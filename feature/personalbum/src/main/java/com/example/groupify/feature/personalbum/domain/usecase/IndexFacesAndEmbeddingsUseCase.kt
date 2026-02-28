// feature/personalbum/src/main/.../domain/usecase/IndexFacesAndEmbeddingsUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.model.Face
import com.example.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IndexFacesAndEmbeddingsUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val faceIndexRepository: FaceIndexRepository,
    private val faceDetector: FaceDetector,
    private val faceEmbedder: FaceEmbedder,
) {
    operator fun invoke(): Flow<IndexingProgress> = flow {
        val allPhotos = photoRepository.getAll().first()
        photoRepository.upsertAll(allPhotos)

        val unindexed = photoRepository.getUnindexed(limit = allPhotos.size)
        val total = unindexed.size

        unindexed.forEachIndexed { index, photo ->
            try {
                val detectedFaces = faceDetector.detectFaces(photo.uri)
                val facesToStore = mutableListOf<Face>()

                for (detectedFace in detectedFaces) {
                    try {
                        val embedding = faceEmbedder.embedFace(photo.uri, detectedFace.boundingBox)
                        facesToStore.add(
                            Face(
                                photoId = photo.id,
                                boundingBox = detectedFace.boundingBox,
                                embedding = embedding,
                            )
                        )
                    } catch (e: Exception) {
                        // Skip this face if embedding fails
                    }
                }

                if (facesToStore.isNotEmpty()) {
                    faceIndexRepository.saveAll(facesToStore)
                }
                photoRepository.markIndexed(photo.id, System.currentTimeMillis())
            } catch (e: Exception) {
                // Skip this photo; still mark as attempted to avoid re-processing indefinitely
                photoRepository.markIndexed(photo.id, System.currentTimeMillis())
            }
            emit(IndexingProgress(current = index + 1, total = total))
        }
    }
}
