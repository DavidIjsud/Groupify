// feature/personalbum/src/main/.../domain/usecase/FindMatchingPhotosUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import com.example.groupify.feature.personalbum.domain.util.cosineSimilarity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class MatchResult(val matchCount: Int, val matchedUris: List<String>)

class FindMatchingPhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val faceIndexRepository: FaceIndexRepository,
    private val faceDetector: FaceDetector,
    private val faceEmbedder: FaceEmbedder,
) {
    suspend operator fun invoke(
        referencePhotoUri: String,
        limit: Int = 200,
        threshold: Float = 0.75f,
    ): MatchResult = withContext(Dispatchers.Default) {
        // 1. Detect + embed reference face
        val referenceFaces = faceDetector.detectFaces(referencePhotoUri)
        if (referenceFaces.isEmpty()) return@withContext MatchResult(0, emptyList())

        val referenceFace = referenceFaces.maxBy { face ->
            val box = face.boundingBox
            (box.right - box.left) * (box.bottom - box.top)
        }

        val referenceEmbedding = faceEmbedder.embedFace(referencePhotoUri, referenceFace.boundingBox)

        // 2. Load all stored embeddings from DB
        val allStoredFaces = faceIndexRepository.getAllFaces().first()

        // 3. Find matching photo IDs via cosine similarity
        val matchedPhotoIds = mutableSetOf<String>()
        for (storedFace in allStoredFaces) {
            if (cosineSimilarity(referenceEmbedding, storedFace.embedding) >= threshold) {
                matchedPhotoIds.add(storedFace.photoId)
            }
        }

        // 4. Resolve photo IDs to URIs
        val matchedUris = matchedPhotoIds
            .take(limit)
            .mapNotNull { photoId -> photoRepository.getById(photoId)?.uri }

        MatchResult(matchCount = matchedUris.size, matchedUris = matchedUris)
    }
}
