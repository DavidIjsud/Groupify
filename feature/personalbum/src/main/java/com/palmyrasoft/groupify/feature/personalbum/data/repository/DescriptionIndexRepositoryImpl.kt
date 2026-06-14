// feature/personalbum/src/main/.../data/repository/DescriptionIndexRepositoryImpl.kt
package com.palmyrasoft.groupify.feature.personalbum.data.repository

import com.palmyrasoft.groupify.feature.personalbum.data.source.LocalDatabasePhotoEmbeddingDataSource
import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoEmbedding
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.DescriptionIndexRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DescriptionIndexRepositoryImpl @Inject constructor(
    private val dataSource: LocalDatabasePhotoEmbeddingDataSource,
) : DescriptionIndexRepository {
    override suspend fun saveAll(embeddings: List<PhotoEmbedding>) = dataSource.insertAll(embeddings)
    override fun getAllEmbeddings(): Flow<List<PhotoEmbedding>> = dataSource.queryAll()
    override fun observeCount(): Flow<Int> = dataSource.observeCount()
}
