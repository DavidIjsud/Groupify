// feature/personalbum/src/main/.../domain/usecase/IndexPhotosUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class IndexingProgress(val processed: Int, val total: Int)

class IndexPhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val faceIndexRepository: FaceIndexRepository,
) {
    operator fun invoke(): Flow<IndexingProgress> = TODO("Scan photos, detect faces, store embeddings")
}
