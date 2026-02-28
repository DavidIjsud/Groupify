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
        val (sourceBitmap, sampleSize) = withContext(Dispatchers.IO) {
            decodeSampledBitmap(photoUri)
        }

        return withContext(Dispatchers.Default) {
            val softBitmap = if (sourceBitmap.config == Bitmap.Config.HARDWARE) {
                sourceBitmap.copy(Bitmap.Config.ARGB_8888, false).also { sourceBitmap.recycle() }
            } else {
                sourceBitmap
            }
            try {
                // Scale bounding box coordinates to match the downsampled bitmap
                val scaledLeft = (faceBoundingBox.left / sampleSize).toInt()
                    .coerceIn(0, softBitmap.width - 1)
                val scaledTop = (faceBoundingBox.top / sampleSize).toInt()
                    .coerceIn(0, softBitmap.height - 1)
                val scaledRight = (faceBoundingBox.right / sampleSize).toInt()
                    .coerceIn(scaledLeft + 1, softBitmap.width)
                val scaledBottom = (faceBoundingBox.bottom / sampleSize).toInt()
                    .coerceIn(scaledTop + 1, softBitmap.height)

                val cropped = Bitmap.createBitmap(
                    softBitmap,
                    scaledLeft,
                    scaledTop,
                    scaledRight - scaledLeft,
                    scaledBottom - scaledTop,
                )
                val resized = Bitmap.createScaledBitmap(cropped, INPUT_SIZE, INPUT_SIZE, true)
                cropped.recycle()

                val inputBuffer = bitmapToInputBuffer(resized)
                resized.recycle()

                inferenceMutex.withLock {
                    val outputShape = interpreter.getOutputTensor(0).shape()
                    val outputSize = when {
                        outputShape.size == 2 && outputShape[0] == 1 -> outputShape[1]
                        outputShape.size == 1 -> outputShape[0]
                        else -> throw IllegalStateException(
                            "Unexpected output tensor shape: ${outputShape.toList()}"
                        )
                    }
                    val outputBuffer = Array(1) { FloatArray(outputSize) }
                    interpreter.run(inputBuffer, outputBuffer)
                    outputBuffer[0]
                }
            } finally {
                softBitmap.recycle()
            }
        }
    }

    /**
     * Two-pass decode: first pass reads dimensions only, second pass decodes
     * at the computed [inSampleSize] so the max dimension stays within [MAX_DECODE_DIMENSION].
     * Returns the bitmap and the actual sample size used (needed to scale bounding boxes).
     */
    private fun decodeSampledBitmap(photoUri: String): Pair<Bitmap, Int> {
        val uri = Uri.parse(photoUri)

        val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, boundsOptions)
        }

        val sampleSize = calculateInSampleSize(
            boundsOptions.outWidth,
            boundsOptions.outHeight,
            MAX_DECODE_DIMENSION,
        )

        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeOptions)
        } ?: throw IllegalStateException("Cannot decode bitmap from $photoUri")

        return bitmap to sampleSize
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

    companion object {
        private const val MODEL_FILENAME = "facenet.tflite"
        private const val INPUT_SIZE = 160
        private const val FLOAT_BYTES = 4
        private const val MEAN = 127.5f
        private const val STD = 127.5f
        private const val MAX_DECODE_DIMENSION = 1024
    }
}
