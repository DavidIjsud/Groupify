package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.model.PhotoMatch
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
    suspend operator fun invoke(queryPhotoUri: String, threshold: Float = 0.75f): List<PhotoMatch> {
        val detectedFaces = faceDetector.detectFaces(queryPhotoUri)
        require(detectedFaces.isNotEmpty()) { "No face detected in the selected photo" }

        val largestFace = detectedFaces.maxByOrNull { face ->
            val bb = face.boundingBox
            (bb.right - bb.left) * (bb.bottom - bb.top)
        }!!

        val queryEmbedding = faceEmbedder.embedFace(queryPhotoUri, largestFace.boundingBox)

        val storedFaces = faceIndexRepository.getAllFaces().first()

        // Keep the best similarity score per photoId (a photo may have multiple faces)
        val bestScoreByPhoto = mutableMapOf<String, Float>()
        for (face in storedFaces) {
            val sim = cosineSimilarity(queryEmbedding, face.embedding)
            if (sim >= threshold) {
                val current = bestScoreByPhoto[face.photoId]
                if (current == null || sim > current) {
                    bestScoreByPhoto[face.photoId] = sim
                }
            }
        }

        if (bestScoreByPhoto.isEmpty()) return emptyList()

        val photos = photoRepository.getByIds(bestScoreByPhoto.keys.toList())
        val photoMap = photos.associateBy { it.id }

        return bestScoreByPhoto.entries
            .mapNotNull { (photoId, score) ->
                val uri = photoMap[photoId]?.uri ?: return@mapNotNull null
                if (uri == queryPhotoUri) return@mapNotNull null
                PhotoMatch(uri = uri, score = score)
            }
            .sortedByDescending { it.score }
    }
}
