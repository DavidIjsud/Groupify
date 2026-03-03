package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.model.BoundingBox
import com.example.groupify.feature.personalbum.domain.model.PhotoMatch
import com.example.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import com.example.groupify.feature.personalbum.domain.util.cosineSimilarity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SearchByPhotoUseCase @Inject constructor(
    private val faceEmbedder: FaceEmbedder,
    private val faceIndexRepository: FaceIndexRepository,
    private val photoRepository: PhotoRepository,
) {
    suspend operator fun invoke(
        queryPhotoUri: String,
        selectedFaces: List<BoundingBox>,
        threshold: Float,
    ): List<PhotoMatch> {
        require(selectedFaces.isNotEmpty()) { "Select at least one face to search" }

        val storedFaces = faceIndexRepository.getAllFaces().first()
        if (storedFaces.isEmpty()) {
            throw IllegalStateException("No indexed faces yet. Run indexing first.")
        }

        // For each selected query face, embed and compare against all stored face embeddings.
        // Track the single best similarity score per photoId across all query faces — this lets
        // a multi-face search return a photo if ANY of the selected faces match it.
        val bestScoreByPhoto = mutableMapOf<String, Float>()
        for (boundingBox in selectedFaces) {
            val queryEmbedding = faceEmbedder.embedFace(queryPhotoUri, boundingBox)
            for (storedFace in storedFaces) {
                // Clamp to [-1, 1] as a safety guard; both embeddings are L2-normalized so
                // this should already hold, but floating-point drift can push outside the range.
                val sim = cosineSimilarity(queryEmbedding, storedFace.embedding)
                    .coerceIn(-1f, 1f)
                if (sim >= threshold) {
                    val current = bestScoreByPhoto[storedFace.photoId]
                    if (current == null || sim > current) {
                        bestScoreByPhoto[storedFace.photoId] = sim
                    }
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
