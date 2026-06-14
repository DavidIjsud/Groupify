// feature/personalbum/src/main/.../data/ml/OnnxClipImageEmbedder.kt
package com.palmyrasoft.groupify.feature.personalbum.data.ml

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import com.palmyrasoft.groupify.feature.personalbum.BuildConfig
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.ImageEmbedder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.nio.FloatBuffer
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Embeds a whole photo into the MobileCLIP image space via ONNX Runtime (the CLIP counterpart of
 * [TFLiteFaceNetEmbedder], which uses LiteRT for faces). Preprocessing follows the model's
 * `preprocessor_config.json`: resize shortest edge to 256, center-crop 256×256, scale to [0,1]
 * (mean 0 / std 1). Output is L2-normalized so cosine similarity equals the dot product.
 */
class OnnxClipImageEmbedder @Inject constructor(
    @ApplicationContext private val context: Context,
) : ImageEmbedder {

    private val inferenceMutex = Mutex()

    private val env: OrtEnvironment by lazy { OrtEnvironment.getEnvironment() }

    private val session: OrtSession by lazy {
        val modelFile = ClipModelFiles.ensure(context, MODEL_ASSET)
        val threads = min(4, Runtime.getRuntime().availableProcessors()).coerceAtLeast(2)
        val opts = OrtSession.SessionOptions().apply {
            setIntraOpNumThreads(threads)
            setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT)
        }
        if (BuildConfig.DEBUG) Log.d(TAG, "ORT vision session created with $threads thread(s)")
        env.createSession(modelFile.absolutePath, opts)
    }

    override suspend fun embedImage(photoUri: String): FloatArray {
        val bitmap = withContext(Dispatchers.IO) {
            BitmapDecodeUtils.decodeSampledAndRotatedBitmap(
                context,
                Uri.parse(photoUri),
                MAX_DECODE_DIMENSION,
            ).first
        }

        return withContext(Dispatchers.Default) {
            val input = preprocess(bitmap)
            bitmap.recycle()

            val tInfer = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

            val raw = inferenceMutex.withLock {
                OnnxTensor.createTensor(env, input, INPUT_SHAPE).use { tensor ->
                    session.run(mapOf(INPUT_NAME to tensor)).use { result ->
                        @Suppress("UNCHECKED_CAST")
                        (result[0].value as Array<FloatArray>)[0]
                    }
                }
            }

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "ORT vision inference in ${SystemClock.elapsedRealtime() - tInfer}ms")
            }

            l2Normalize(raw)
        }
    }

    /**
     * Resize shortest edge → 256, center-crop 256×256, then pack as NCHW float [1,3,256,256] with
     * each channel scaled to [0,1].
     */
    private fun preprocess(src: Bitmap): FloatBuffer {
        val scale = INPUT_SIZE.toFloat() / min(src.width, src.height)
        val scaledW = (src.width * scale).roundToInt().coerceAtLeast(INPUT_SIZE)
        val scaledH = (src.height * scale).roundToInt().coerceAtLeast(INPUT_SIZE)

        val scaled = if (scaledW == src.width && scaledH == src.height) {
            src
        } else {
            Bitmap.createScaledBitmap(src, scaledW, scaledH, true)
        }

        val xOff = ((scaledW - INPUT_SIZE) / 2).coerceIn(0, scaledW - INPUT_SIZE)
        val yOff = ((scaledH - INPUT_SIZE) / 2).coerceIn(0, scaledH - INPUT_SIZE)
        val cropped = Bitmap.createBitmap(scaled, xOff, yOff, INPUT_SIZE, INPUT_SIZE)

        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        cropped.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)

        if (cropped !== scaled) cropped.recycle()
        if (scaled !== src) scaled.recycle()

        val plane = INPUT_SIZE * INPUT_SIZE
        val buffer = FloatBuffer.allocate(3 * plane)
        for (i in 0 until plane) {
            val p = pixels[i]
            buffer.put(i, ((p shr 16) and 0xFF) / 255f)             // R plane
            buffer.put(plane + i, ((p shr 8) and 0xFF) / 255f)      // G plane
            buffer.put(2 * plane + i, (p and 0xFF) / 255f)          // B plane
        }
        buffer.rewind()
        return buffer
    }

    private fun l2Normalize(v: FloatArray): FloatArray {
        var normSq = 0f
        for (x in v) normSq += x * x
        val norm = sqrt(normSq)
        if (norm < 1e-10f) return v
        return FloatArray(v.size) { i -> v[i] / norm }
    }

    companion object {
        private const val TAG = "OnnxClipImageEmbedder"
        private const val MODEL_ASSET = "clip/vision_model.onnx"
        private const val INPUT_NAME = "pixel_values"
        private const val INPUT_SIZE = 256
        private const val MAX_DECODE_DIMENSION = 512
        private val INPUT_SHAPE = longArrayOf(1, 3, INPUT_SIZE.toLong(), INPUT_SIZE.toLong())
    }
}
