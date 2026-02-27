// feature/personalbum/src/main/.../data/ml/MlKitFaceDetector.kt
package com.example.groupify.feature.personalbum.data.ml

import android.content.Context
import android.net.Uri
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
        val image = withContext(Dispatchers.IO) {
            InputImage.fromFilePath(context, Uri.parse(photoUri))
        }
        return suspendCancellableCoroutine { continuation ->
            detector.process(image)
                .addOnSuccessListener { mlFaces ->
                    val result = mlFaces.map { face ->
                        val rect = face.boundingBox
                        DetectedFace(
                            boundingBox = BoundingBox(
                                left = rect.left.toFloat(),
                                top = rect.top.toFloat(),
                                right = rect.right.toFloat(),
                                bottom = rect.bottom.toFloat(),
                            ),
                            trackingId = face.trackingId,
                            smilingProbability = face.smilingProbability,
                            leftEyeOpenProbability = face.leftEyeOpenProbability,
                            rightEyeOpenProbability = face.rightEyeOpenProbability,
                        )
                    }
                    continuation.resume(result)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
}
