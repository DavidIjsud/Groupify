// feature/personalbum/src/main/.../data/ml/TFLiteFaceNetEmbedder.kt
package com.example.groupify.feature.personalbum.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
        val sourceBitmap = withContext(Dispatchers.IO) {
            val options = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            context.contentResolver.openInputStream(Uri.parse(photoUri))?.use { stream ->
                BitmapFactory.decodeStream(stream, null, options)
            } ?: throw IllegalStateException("Cannot decode bitmap from $photoUri")
        }

        return withContext(Dispatchers.Default) {
            val softBitmap = if (sourceBitmap.config == Bitmap.Config.HARDWARE) {
                sourceBitmap.copy(Bitmap.Config.ARGB_8888, false).also { sourceBitmap.recycle() }
            } else {
                sourceBitmap
            }
            try {
                val left = faceBoundingBox.left.toInt().coerceIn(0, softBitmap.width - 1)
                val top = faceBoundingBox.top.toInt().coerceIn(0, softBitmap.height - 1)
                val right = faceBoundingBox.right.toInt().coerceIn(left + 1, softBitmap.width)
                val bottom = faceBoundingBox.bottom.toInt().coerceIn(top + 1, softBitmap.height)

                val cropped = Bitmap.createBitmap(softBitmap, left, top, right - left, bottom - top)
                val resized = Bitmap.createScaledBitmap(cropped, INPUT_SIZE, INPUT_SIZE, true)
                cropped.recycle()

                val inputBuffer = bitmapToInputBuffer(resized)
                resized.recycle()

                inferenceMutex.withLock {
                    val outputShape = interpreter.getOutputTensor(0).shape()
                    val outputSize = outputShape[1]
                    val outputBuffer = Array(1) { FloatArray(outputSize) }
                    interpreter.run(inputBuffer, outputBuffer)
                    outputBuffer[0]
                }
            } finally {
                softBitmap.recycle()
            }
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

    companion object {
        private const val MODEL_FILENAME = "facenet.tflite"
        private const val INPUT_SIZE = 160
        private const val FLOAT_BYTES = 4
        private const val MEAN = 127.5f
        private const val STD = 127.5f
    }
}
