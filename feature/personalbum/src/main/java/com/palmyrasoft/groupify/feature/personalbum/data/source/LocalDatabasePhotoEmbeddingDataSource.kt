// feature/personalbum/src/main/.../data/source/LocalDatabasePhotoEmbeddingDataSource.kt
package com.palmyrasoft.groupify.feature.personalbum.data.source

import com.palmyrasoft.groupify.feature.personalbum.data.local.Converters
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoEmbeddingDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PhotoEmbeddingEntity
import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoEmbedding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDatabasePhotoEmbeddingDataSource @Inject constructor(
    private val dao: PhotoEmbeddingDao,
) {
    suspend fun insertAll(embeddings: List<PhotoEmbedding>) {
        dao.insertAll(embeddings.map { it.toEntity() })
    }

    fun queryAll(): Flow<List<PhotoEmbedding>> =
        dao.getAllEmbeddings().map { entities -> entities.map { it.toDomain() } }

    fun observeCount(): Flow<Int> = dao.countFlow()
}

private fun PhotoEmbedding.toEntity(): PhotoEmbeddingEntity = PhotoEmbeddingEntity(
    photoId = photoId,
    uri = uri,
    embeddingBlob = Converters.floatArrayToByteArray(embedding),
    createdAt = System.currentTimeMillis(),
)

private fun PhotoEmbeddingEntity.toDomain(): PhotoEmbedding = PhotoEmbedding(
    photoId = photoId,
    uri = uri,
    embedding = Converters.byteArrayToFloatArray(embeddingBlob),
)
