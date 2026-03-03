// feature/personalbum/src/main/.../data/ml/TFLiteFaceNetEmbedder.kt
package com.example.groupify.feature.personalbum.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
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
import kotlin.math.sqrt

class TFLiteFaceNetEmbedder @Inject constructor(
    @ApplicationContext private val context: Context,
) : FaceEmbedder {

    private val inferenceMutex = Mutex()

    private val interpreter: Interpreter by lazy {
        Interpreter(loadModelBuffer())
    }

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
        // Decode, downsample, and EXIF-rotate on IO dispatcher.
        // The returned bitmap is in the same coordinate space as the bounding boxes produced
        // by MlKitFaceDetector (which also applies EXIF rotation before running detection).
        val (sourceBitmap, sampleSize) = withContext(Dispatchers.IO) {
            decodeSampledAndRotatedBitmap(photoUri)
        }

        return withContext(Dispatchers.Default) {
            // Guard against HARDWARE config (should not occur after our decode, but be safe).
            val softBitmap = if (sourceBitmap.config == Bitmap.Config.HARDWARE) {
                sourceBitmap.copy(Bitmap.Config.ARGB_8888, false).also { sourceBitmap.recycle() }
            } else {
                sourceBitmap
            }
            try {
                // Scale bounding box coordinates to match the downsampled, rotated bitmap.
                // Bbox coords are in the rotated-full-resolution space; dividing by sampleSize
                // maps them into the downsampled-rotated space — matching both axes correctly.
                val scaledLeft = (faceBoundingBox.left / sampleSize).toInt()
                    .coerceIn(0, softBitmap.width - 1)
                val scaledTop = (faceBoundingBox.top / sampleSize).toInt()
                    .coerceIn(0, softBitmap.height - 1)
                val scaledRight = (faceBoundingBox.right / sampleSize).toInt()
                    .coerceIn(scaledLeft + 1, softBitmap.width)
                val scaledBottom = (faceBoundingBox.bottom / sampleSize).toInt()
                    .coerceIn(scaledTop + 1, softBitmap.height)

                val cropWidth = scaledRight - scaledLeft
                val cropHeight = scaledBottom - scaledTop

                // Reject crops that are too small to produce meaningful embeddings.
                if (cropWidth < MIN_CROP_SIZE || cropHeight < MIN_CROP_SIZE) {
                    throw IllegalStateException(
                        "Face crop too small (${cropWidth}x${cropHeight}); bounding box may be misaligned.",
                    )
                }

                val cropped = Bitmap.createBitmap(
                    softBitmap,
                    scaledLeft,
                    scaledTop,
                    cropWidth,
                    cropHeight,
                )
                val resized = Bitmap.createScaledBitmap(cropped, INPUT_SIZE, INPUT_SIZE, true)
                cropped.recycle()

                val inputBuffer = bitmapToInputBuffer(resized)
                resized.recycle()

                val raw = inferenceMutex.withLock {
                    val outputShape = interpreter.getOutputTensor(0).shape()
                    val outputSize = when {
                        outputShape.size == 2 && outputShape[0] == 1 -> outputShape[1]
                        outputShape.size == 1 -> outputShape[0]
                        else -> throw IllegalStateException(
                            "Unexpected output tensor shape: ${outputShape.toList()}",
                        )
                    }
                    val outputBuffer = Array(1) { FloatArray(outputSize) }
                    interpreter.run(inputBuffer, outputBuffer)
                    outputBuffer[0]
                }

                // L2-normalize so downstream cosine similarity == dot product on unit vectors.
                l2Normalize(raw)
            } finally {
                softBitmap.recycle()
            }
        }
    }

    /**
     * Two-pass decode (inSampleSize bounds memory) followed by EXIF rotation.
     *
     * This mirrors the decode + rotate sequence in [MlKitFaceDetector] so the resulting bitmap
     * shares the same pixel coordinate space as the bounding boxes ML Kit produced.
     *
     * Returns the ARGB_8888 rotated bitmap and the sample size used (needed to scale bboxes).
     */
    private fun decodeSampledAndRotatedBitmap(photoUri: String): Pair<Bitmap, Int> {
        val uri = Uri.parse(photoUri)

        // Pass 1 — read dimensions only.
        val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, boundsOptions)
        }

        val sampleSize = calculateInSampleSize(
            boundsOptions.outWidth,
            boundsOptions.outHeight,
            MAX_DECODE_DIMENSION,
        )

        // Pass 2 — decode at the computed sample size.
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val decoded = context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeOptions)
        } ?: throw IllegalStateException("Cannot decode bitmap from $photoUri")

        // Pass 3 — read EXIF orientation (same mapping as MlKitFaceDetector).
        val rotationDegrees = context.contentResolver.openInputStream(uri)?.use { stream ->
            val exif = ExifInterface(stream)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        } ?: 0f

        // Apply rotation (if any), recycling the pre-rotation bitmap when a new one is made.
        val rotated = if (rotationDegrees != 0f) {
            val matrix = Matrix().apply { postRotate(rotationDegrees) }
            Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
                .also { if (it !== decoded) decoded.recycle() }
        } else {
            decoded
        }

        // Guarantee ARGB_8888 (rotation might produce a different config on some devices).
        val argb = if (rotated.config != Bitmap.Config.ARGB_8888) {
            rotated.copy(Bitmap.Config.ARGB_8888, false).also { rotated.recycle() }
        } else {
            rotated
        }

        return argb to sampleSize
    }

    private fun calculateInSampleSize(width: Int, height: Int, maxDimension: Int): Int {
        var sampleSize = 1
        while (maxOf(width / sampleSize, height / sampleSize) > maxDimension) {
            sampleSize *= 2
        }
        return sampleSize
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
     * L2-normalizes [v] in-place equivalent, returning a new array on the unit hypersphere.
     * Cosine similarity on unit vectors equals their dot product, and FaceNet embeddings are
     * conventionally used this way. If the norm is essentially zero, the original is returned
     * unchanged to avoid division-by-zero.
     */
    private fun l2Normalize(v: FloatArray): FloatArray {
        var normSq = 0f
        for (x in v) normSq += x * x
        val norm = sqrt(normSq)
        if (norm < 1e-10f) return v
        return FloatArray(v.size) { i -> v[i] / norm }
    }

    companion object {
        private const val MODEL_FILENAME = "facenet.tflite"
        private const val INPUT_SIZE = 160
        private const val FLOAT_BYTES = 4
        private const val MEAN = 127.5f
        private const val STD = 127.5f
        private const val MAX_DECODE_DIMENSION = 1024
        private const val MIN_CROP_SIZE = 20
    }
}
