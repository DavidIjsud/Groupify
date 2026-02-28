// feature/personalbum/src/main/.../domain/usecase/FindMatchingPhotosUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import com.example.groupify.feature.personalbum.domain.util.cosineSimilarity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class MatchResult(val matchCount: Int, val matchedUris: List<String>)

class FindMatchingPhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val faceDetector: FaceDetector,
    private val faceEmbedder: FaceEmbedder,
) {
    suspend operator fun invoke(
        referencePhotoUri: String,
        limit: Int = 200,
        threshold: Float = 0.75f,
    ): MatchResult = withContext(Dispatchers.Default) {
        val referenceFaces = faceDetector.detectFaces(referencePhotoUri)
        if (referenceFaces.isEmpty()) return@withContext MatchResult(0, emptyList())

        val referenceFace = referenceFaces.maxBy { face ->
            val box = face.boundingBox
            (box.right - box.left) * (box.bottom - box.top)
        }

        val referenceEmbedding = faceEmbedder.embedFace(referencePhotoUri, referenceFace.boundingBox)

        val photos = photoRepository.getAll().first().take(limit)

        val matchedUris = mutableListOf<String>()
        for (photo in photos) {
            try {
                val faces = faceDetector.detectFaces(photo.uri)
                var matched = false
                for (face in faces) {
                    if (matched) break
                    val embedding = faceEmbedder.embedFace(photo.uri, face.boundingBox)
                    if (cosineSimilarity(referenceEmbedding, embedding) >= threshold) {
                        matchedUris.add(photo.uri)
                        matched = true
                    }
                }
            } catch (e: Exception) {
                // Skip photos that fail to decode or detect; continue with the rest
            }
        }

        MatchResult(matchCount = matchedUris.size, matchedUris = matchedUris)
    }
}
