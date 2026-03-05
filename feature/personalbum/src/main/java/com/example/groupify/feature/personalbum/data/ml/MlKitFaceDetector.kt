// feature/personalbum/src/main/java/com/example/groupify/feature/personalbum/data/ml/MlKitFaceDetector.kt
package com.example.groupify.feature.personalbum.data.ml

import android.content.Context
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import com.example.groupify.feature.personalbum.BuildConfig
import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.model.BoundingBox
import com.example.groupify.feature.personalbum.domain.model.DetectedFace
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MlKitFaceDetector @Inject constructor(
    @ApplicationContext private val context: Context,
) : FaceDetector {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setMinFaceSize(0.1f)
        .build()

    private val detector = FaceDetection.getClient(options)

    override suspend fun detectFaces(photoUri: String): List<DetectedFace> {
        // Decode a bounded, EXIF-rotated bitmap on IO, then hand it to ML Kit.
        // Using BitmapDecodeUtils (max 1024 px) avoids loading full-resolution images —
        // e.g. a 12MP photo drops from ~48 MB to ~3 MB for detection.
        //
        // ML Kit returns bounding boxes in the coordinate space of the bitmap it receives.
        // We scale them back up by sampleSize so callers (e.g. TFLiteFaceNetEmbedder) always
        // see bboxes in full-resolution coordinates, preserving the existing contract.
        val tDecode = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

        val (sampledBitmap, sampleSize) = withContext(Dispatchers.IO) {
            BitmapDecodeUtils.decodeSampledAndRotatedBitmap(
                context,
                Uri.parse(photoUri),
                MAX_DECODE_DIMENSION,
            )
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "detect/decode in ${SystemClock.elapsedRealtime() - tDecode}ms — " +
                "${sampledBitmap.width}x${sampledBitmap.height} sampleSize=$sampleSize")
        }

        val image = InputImage.fromBitmap(sampledBitmap, 0)

        val tDetect = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation { /* ML Kit task cannot be cancelled; isActive guards resume */ }

            detector.process(image)
                .addOnSuccessListener { mlFaces ->
                    // Recycle the sampled bitmap now that ML Kit has finished reading it.
                    sampledBitmap.recycle()

                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "ML Kit detection in ${SystemClock.elapsedRealtime() - tDetect}ms, " +
                            "found ${mlFaces.size} face(s)")
                    }

                    if (continuation.isActive) {
                        val result = mlFaces.map { face ->
                            val rect = face.boundingBox
                            // Scale bbox coordinates back to the full-resolution space so that
                            // downstream consumers (embedder) can use them without knowing the
                            // sample size that was applied here.
                            DetectedFace(
                                boundingBox = BoundingBox(
                                    left = rect.left.toFloat() * sampleSize,
                                    top = rect.top.toFloat() * sampleSize,
                                    right = rect.right.toFloat() * sampleSize,
                                    bottom = rect.bottom.toFloat() * sampleSize,
                                ),
                                trackingId = face.trackingId,
                                smilingProbability = face.smilingProbability,
                                leftEyeOpenProbability = face.leftEyeOpenProbability,
                                rightEyeOpenProbability = face.rightEyeOpenProbability,
                            )
                        }
                        continuation.resume(result)
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
        private const val TAG = "MlKitFaceDetector"

        // Match the embedder's MAX_DECODE_DIMENSION so the round-trip (scale up in detector,
        // scale down in embedder) uses the same sampleSize for both passes.
        private const val MAX_DECODE_DIMENSION = 1024
    }
}
