package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.model.QueryFace
import javax.inject.Inject

class DetectQueryFacesUseCase @Inject constructor(
    private val faceDetector: FaceDetector,
) {
    suspend operator fun invoke(photoUri: String): List<QueryFace> {
        val detected = faceDetector.detectFaces(photoUri)
        if (detected.isEmpty()) throw IllegalStateException("No faces detected in the selected photo")

        val base = photoUri.hashCode()
        return detected
            .sortedByDescending { face ->
                val bb = face.boundingBox
                (bb.right - bb.left) * (bb.bottom - bb.top)
            }
            .mapIndexed { index, face ->
                QueryFace(id = (base * 31) + index, boundingBox = face.boundingBox)
            }
    }
}
