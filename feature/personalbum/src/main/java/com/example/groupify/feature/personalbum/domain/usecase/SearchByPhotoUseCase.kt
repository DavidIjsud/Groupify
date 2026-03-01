// feature/personalbum/src/main/java/com/example/groupify/feature/personalbum/domain/usecase/SearchByPhotoUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import com.example.groupify.feature.personalbum.domain.util.cosineSimilarity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SearchByPhotoUseCase @Inject constructor(
    private val faceDetector: FaceDetector,
    private val faceEmbedder: FaceEmbedder,
    private val faceIndexRepository: FaceIndexRepository,
    private val photoRepository: PhotoRepository,
) {
    suspend operator fun invoke(queryPhotoUri: String, threshold: Float = 0.75f): List<String> {
        val detectedFaces = faceDetector.detectFaces(queryPhotoUri)
        require(detectedFaces.isNotEmpty()) { "No faces detected in the selected photo" }

        val largestFace = detectedFaces.maxByOrNull { face ->
            val bb = face.boundingBox
            (bb.right - bb.left) * (bb.bottom - bb.top)
        }!!

        val queryEmbedding = faceEmbedder.embedFace(queryPhotoUri, largestFace.boundingBox)

        val storedFaces = faceIndexRepository.getAllFaces().first()
        val matchedPhotoIds = storedFaces
            .filter { face -> cosineSimilarity(queryEmbedding, face.embedding) >= threshold }
            .map { it.photoId }
            .distinct()

        val photos = photoRepository.getByIds(matchedPhotoIds)
        val photoMap = photos.associateBy { it.id }
        return matchedPhotoIds.mapNotNull { photoMap[it]?.uri }
    }
}
