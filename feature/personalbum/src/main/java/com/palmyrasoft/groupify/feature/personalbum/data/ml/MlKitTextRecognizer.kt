// feature/personalbum/src/main/java/com/palmyrasoft/groupify/feature/personalbum/data/ml/MlKitTextRecognizer.kt
package com.palmyrasoft.groupify.feature.personalbum.data.ml

import android.content.Context
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import com.palmyrasoft.groupify.feature.personalbum.BuildConfig
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.TextRecognizer
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * On-device OCR via ML Kit Text Recognition v2 (Latin script).
 *
 * Mirrors [MlKitFaceDetector]: decode a bounded, EXIF-rotated bitmap off the main thread via
 * [BitmapDecodeUtils], hand it to ML Kit, and return only the flattened recognized text. Unlike
 * the face detector we don't need bounding boxes, so no coordinate scaling is performed.
 */
class MlKitTextRecognizer @Inject constructor(
    @ApplicationContext private val context: Context,
) : TextRecognizer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override suspend fun recognizeText(photoUri: String): String {
        val tDecode = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

        val (sampledBitmap, sampleSize) = withContext(Dispatchers.IO) {
            BitmapDecodeUtils.decodeSampledAndRotatedBitmap(
                context,
                Uri.parse(photoUri),
                MAX_DECODE_DIMENSION,
            )
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "ocr/decode in ${SystemClock.elapsedRealtime() - tDecode}ms — " +
                "${sampledBitmap.width}x${sampledBitmap.height} sampleSize=$sampleSize")
        }

        val image = InputImage.fromBitmap(sampledBitmap, 0)

        val tOcr = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation { /* ML Kit task cannot be cancelled; isActive guards resume */ }

            recognizer.process(image)
                .addOnSuccessListener { result ->
                    sampledBitmap.recycle()

                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "ML Kit OCR in ${SystemClock.elapsedRealtime() - tOcr}ms, " +
                            "${result.text.length} char(s)")
                    }

                    if (continuation.isActive) {
                        // Flatten newlines/whitespace so the FTS index treats the text as a
                        // single searchable blob.
                        continuation.resume(result.text.replace(Regex("\\s+"), " ").trim())
                    }
                }
                .addOnFailureListener { e ->
                    sampledBitmap.recycle()
                    if (continuation.isActive) {
                        continuation.resumeWithException(e)
                    }
                }
        }
    }

    companion object {
        private const val TAG = "MlKitTextRecognizer"

        // Use the same decode budget as the face pipeline so the same EXIF-rotated bitmap cost
        // model applies; 1024 px is ample for OCR of signage / documents in typical photos.
        private const val MAX_DECODE_DIMENSION = 1024
    }
}
