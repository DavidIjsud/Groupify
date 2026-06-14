// feature/personalbum/src/main/.../data/ml/OnnxClipTextEmbedder.kt
package com.palmyrasoft.groupify.feature.personalbum.data.ml

import ai.onnxruntime.OnnxJavaType
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.TensorInfo
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.palmyrasoft.groupify.feature.personalbum.BuildConfig
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.TextQueryEmbedder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.nio.IntBuffer
import java.nio.LongBuffer
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Embeds a text query into the MobileCLIP text space via ONNX Runtime, so it can be cosine-matched
 * against the photo embeddings produced by [OnnxClipImageEmbedder]. Tokenization is delegated to
 * [ClipBpeTokenizer]; the resulting 77-token `input_ids` are fed to the text encoder. Output is
 * L2-normalized.
 */
class OnnxClipTextEmbedder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenizer: ClipBpeTokenizer,
) : TextQueryEmbedder {

    private val inferenceMutex = Mutex()

    private val env: OrtEnvironment by lazy { OrtEnvironment.getEnvironment() }

    private val session: OrtSession by lazy {
        val modelFile = ClipModelFiles.ensure(context, MODEL_ASSET)
        val threads = min(4, Runtime.getRuntime().availableProcessors()).coerceAtLeast(2)
        val opts = OrtSession.SessionOptions().apply {
            setIntraOpNumThreads(threads)
            setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT)
        }
        if (BuildConfig.DEBUG) Log.d(TAG, "ORT text session created with $threads thread(s)")
        env.createSession(modelFile.absolutePath, opts)
    }

    // Some CLIP ONNX exports expect int64 input_ids, others int32 — detect once from the graph.
    private val inputIsLong: Boolean by lazy {
        (session.inputInfo[INPUT_NAME]?.info as? TensorInfo)?.type == OnnxJavaType.INT64
    }

    override suspend fun embedQuery(text: String): FloatArray = withContext(Dispatchers.Default) {
        val tokens = tokenizer.tokenize(text, ClipBpeTokenizer.CONTEXT_LENGTH)

        val tInfer = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

        val raw = inferenceMutex.withLock {
            createInputTensor(tokens).use { tensor ->
                session.run(mapOf(INPUT_NAME to tensor)).use { result ->
                    @Suppress("UNCHECKED_CAST")
                    (result[0].value as Array<FloatArray>)[0]
                }
            }
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "ORT text inference in ${SystemClock.elapsedRealtime() - tInfer}ms")
        }

        l2Normalize(raw)
    }

    private fun createInputTensor(tokens: IntArray): OnnxTensor {
        val shape = longArrayOf(1, tokens.size.toLong())
        return if (inputIsLong) {
            val buffer = LongBuffer.allocate(tokens.size)
            for (t in tokens) buffer.put(t.toLong())
            buffer.rewind()
            OnnxTensor.createTensor(env, buffer, shape)
        } else {
            val buffer = IntBuffer.allocate(tokens.size)
            for (t in tokens) buffer.put(t)
            buffer.rewind()
            OnnxTensor.createTensor(env, buffer, shape)
        }
    }

    private fun l2Normalize(v: FloatArray): FloatArray {
        var normSq = 0f
        for (x in v) normSq += x * x
        val norm = sqrt(normSq)
        if (norm < 1e-10f) return v
        return FloatArray(v.size) { i -> v[i] / norm }
    }

    companion object {
        private const val TAG = "OnnxClipTextEmbedder"
        private const val MODEL_ASSET = "clip/text_model.onnx"
        private const val INPUT_NAME = "input_ids"
    }
}
