// feature/personalbum/src/main/.../data/ml/TFLiteFaceNetEmbedder.kt
package com.example.groupify.feature.personalbum.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.util.Log
import com.example.groupify.feature.personalbum.BuildConfig
import com.example.groupify.feature.personalbum.domain.model.BoundingBox
import com.example.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.sqrt

class TFLiteFaceNetEmbedder @Inject constructor(
    @ApplicationContext private val context: Context,
) : FaceEmbedder {

    // Serializes TFLite inference — the Interpreter is not thread-safe.
    private val inferenceMutex = Mutex()

    // Optimization (2): configure TFLite to use multiple threads.
    // min(4, availableProcessors) keeps it predictable on mid-range devices;
    // coerceAtLeast(2) ensures at least two threads on any device that has them.
    private val interpreter: Interpreter by lazy {
        val threadCount = min(4, Runtime.getRuntime().availableProcessors()).coerceAtLeast(2)
        val opts = Interpreter.Options().apply { setNumThreads(threadCount) }
        if (BuildConfig.DEBUG) Log.d(TAG, "TFLite interpreter created with $threadCount thread(s)")
        Interpreter(loadModelBuffer(), opts)
    }

    // Optimization (1): per-photo bitmap cache.
    // During sequential indexing all faces in the same photo call embedFace() with the same URI.
    // Caching the decoded+rotated bitmap avoids re-opening, re-decoding, and re-rotating it for
    // every face.  The cache holds exactly one entry (the last URI processed).
    //
    // Thread-safety note: during indexing the use case calls embedFace() sequentially on a single
    // coroutine, so there is no concurrent access.  The decodeMutex is a safety net for any
    // future parallel use.
    private val decodeMutex = Mutex()
    private var cachedUri: String? = null
    private var cachedBitmap: Bitmap? = null
    private var cachedSampleSize: Int = 1

    private fun loadModelBuffer(): ByteBuffer {
        val assetFd = context.assets.openFd(MODEL_FILENAME)
        return try {
            FileInputStream(assetFd.fileDescriptor).channel.map(
                FileChannel.MapMode.READ_ONLY,
                assetFd.startOffset,
                assetFd.declaredLength,
            )
        } finally {
            assetFd.close()
        }
    }

    override suspend fun embedFace(photoUri: String, faceBoundingBox: BoundingBox): FloatArray {
        // Step 1 — obtain the sampled+rotated source bitmap (cache hit = no IO).
        val (sourceBitmap, sampleSize) = withContext(Dispatchers.IO) {
            getOrDecodeBitmap(photoUri)
        }

        return withContext(Dispatchers.Default) {
            // Guard against HARDWARE config (API 26+); should not occur after our decode, but be safe.
            val softBitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                sourceBitmap.config == Bitmap.Config.HARDWARE
            ) {
                sourceBitmap.copy(Bitmap.Config.ARGB_8888, false)
                // Note: do NOT recycle sourceBitmap here — it's held in the cache.
            } else {
                sourceBitmap
            }

            // Step 2 — map full-res bbox coordinates into the sampled bitmap space.
            // Bboxes from MlKitFaceDetector are scaled to full-res; dividing by sampleSize
            // maps them back into the downsampled pixel coordinate space used here.
            val scaledLeft = (faceBoundingBox.left / sampleSize).toInt().coerceIn(0, softBitmap.width - 1)
            val scaledTop = (faceBoundingBox.top / sampleSize).toInt().coerceIn(0, softBitmap.height - 1)
            val scaledRight = (faceBoundingBox.right / sampleSize).toInt().coerceIn(scaledLeft + 1, softBitmap.width)
            val scaledBottom = (faceBoundingBox.bottom / sampleSize).toInt().coerceIn(scaledTop + 1, softBitmap.height)

            val cropWidth = scaledRight - scaledLeft
            val cropHeight = scaledBottom - scaledTop

            if (cropWidth < MIN_CROP_SIZE || cropHeight < MIN_CROP_SIZE) {
                throw IllegalStateException(
                    "Face crop too small (${cropWidth}x${cropHeight}); bounding box may be misaligned.",
                )
            }

            // Step 3 — crop, resize to 160×160, run inference.
            val cropped = Bitmap.createBitmap(softBitmap, scaledLeft, scaledTop, cropWidth, cropHeight)
            val resized = Bitmap.createScaledBitmap(cropped, INPUT_SIZE, INPUT_SIZE, true)
            cropped.recycle()

            val inputBuffer = bitmapToInputBuffer(resized)
            resized.recycle()

            val tInfer = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

            val raw = inferenceMutex.withLock {
                val outputShape = interpreter.getOutputTensor(0).shape()
                val outputSize = when {
                    outputShape.size == 2 && outputShape[0] == 1 -> outputShape[1]
                    outputShape.size == 1 -> outputShape[0]
                    else -> throw IllegalStateException("Unexpected output tensor shape: ${outputShape.toList()}")
                }
                val outputBuffer = Array(1) { FloatArray(outputSize) }
                interpreter.run(inputBuffer, outputBuffer)
                outputBuffer[0]
            }

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "TFLite inference in ${SystemClock.elapsedRealtime() - tInfer}ms")
            }

            // Recycle the HARDWARE-copy if we made one.
            if (softBitmap !== sourceBitmap) softBitmap.recycle()

            l2Normalize(raw)
        }
    }

    /**
     * Returns the cached (bitmap, sampleSize) for [photoUri], decoding on cache miss.
     *
     * The old cached bitmap is recycled when the URI changes, so memory stays bounded to
     * a single decoded image at a time.
     *
     * Must be called on a dispatcher that allows IO (called inside withContext(Dispatchers.IO)).
     */
    private suspend fun getOrDecodeBitmap(photoUri: String): Pair<Bitmap, Int> =
        decodeMutex.withLock {
            if (cachedUri == photoUri && cachedBitmap?.isRecycled == false) {
                // Cache hit — same photo, reuse the already-decoded bitmap.
                cachedBitmap!! to cachedSampleSize
            } else {
                // Cache miss — recycle stale entry and decode fresh.
                cachedBitmap?.recycle()

                val tDecode = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

                val (bitmap, sampleSize) = BitmapDecodeUtils.decodeSampledAndRotatedBitmap(
                    context,
                    Uri.parse(photoUri),
                    MAX_DECODE_DIMENSION,
                )

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "embedder decode+rotate ${bitmap.width}x${bitmap.height} " +
                        "sampleSize=$sampleSize in ${SystemClock.elapsedRealtime() - tDecode}ms (cache miss)")
                }

                cachedUri = photoUri
                cachedBitmap = bitmap
                cachedSampleSize = sampleSize
                bitmap to sampleSize
            }
        }

    private fun bitmapToInputBuffer(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(1 * INPUT_SIZE * INPUT_SIZE * 3 * FLOAT_BYTES)
        buffer.order(ByteOrder.nativeOrder())
        for (y in 0 until INPUT_SIZE) {
            for (x in 0 until INPUT_SIZE) {
                val pixel = bitmap.getPixel(x, y)
                buffer.putFloat(((pixel shr 16 and 0xFF).toFloat() - MEAN) / STD)
                buffer.putFloat(((pixel shr 8 and 0xFF).toFloat() - MEAN) / STD)
                buffer.putFloat(((pixel and 0xFF).toFloat() - MEAN) / STD)
            }
        }
        buffer.rewind()
        return buffer
    }

    /**
     * L2-normalises [v] so downstream cosine similarity equals the dot product.
     * Returns [v] unchanged if the norm is effectively zero to avoid division by zero.
     */
    private fun l2Normalize(v: FloatArray): FloatArray {
        var normSq = 0f
        for (x in v) normSq += x * x
        val norm = sqrt(normSq)
        if (norm < 1e-10f) return v
        return FloatArray(v.size) { i -> v[i] / norm }
    }

    companion object {
        private const val TAG = "TFLiteFaceNetEmbedder"
        private const val MODEL_FILENAME = "facenet.tflite"
        private const val INPUT_SIZE = 160
        private const val FLOAT_BYTES = 4
        private const val MEAN = 127.5f
        private const val STD = 127.5f
        private const val MAX_DECODE_DIMENSION = 1024
        private const val MIN_CROP_SIZE = 20
    }
}
