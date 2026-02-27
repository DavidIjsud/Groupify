// feature/personalbum/src/main/.../domain/usecase/DetectFacesInPhotoUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.model.DetectedFace
import javax.inject.Inject

class DetectFacesInPhotoUseCase @Inject constructor(
    private val faceDetector: FaceDetector,
) {
    suspend operator fun invoke(photoUri: String): List<DetectedFace> =
        faceDetector.detectFaces(photoUri)
}
