// feature/personalbum/src/main/.../domain/usecase/IndexPhotosUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.model.Photo
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class IndexingProgress(val current: Int, val total: Int)

class IndexPhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
) {
    operator fun invoke(): Flow<IndexingProgress> = flow {
        val photos: List<Photo> = photoRepository.getAll().first()
        val total = photos.size
        photos.forEachIndexed { index, _ ->
            emit(IndexingProgress(current = index + 1, total = total))
        }
    }
}
