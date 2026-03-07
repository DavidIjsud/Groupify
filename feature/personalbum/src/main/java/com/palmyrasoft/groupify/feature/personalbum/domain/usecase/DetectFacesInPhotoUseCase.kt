// feature/personalbum/src/main/.../domain/usecase/DetectFacesInPhotoUseCase.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.usecase

import com.palmyrasoft.groupify.feature.personalbum.domain.detection.FaceDetector
import com.palmyrasoft.groupify.feature.personalbum.domain.model.DetectedFace
import javax.inject.Inject

class DetectFacesInPhotoUseCase @Inject constructor(
    private val faceDetector: FaceDetector,
) {
    suspend operator fun invoke(photoUri: String): List<DetectedFace> =
        faceDetector.detectFaces(photoUri)
}
