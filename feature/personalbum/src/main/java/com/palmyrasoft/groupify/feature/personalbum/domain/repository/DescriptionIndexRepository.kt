// feature/personalbum/src/main/.../domain/repository/DescriptionIndexRepository.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.repository

import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoEmbedding
import kotlinx.coroutines.flow.Flow

/**
 * On-device index of CLIP photo embeddings for the description-search flow. Sibling of
 * [FaceIndexRepository]; ranking is done in [SearchByDescriptionUseCase] against the full set
 * returned by [getAllEmbeddings] (mirrors how face search scans all stored faces).
 */
interface DescriptionIndexRepository {
    suspend fun saveAll(embeddings: List<PhotoEmbedding>)

    fun getAllEmbeddings(): Flow<List<PhotoEmbedding>>

    /** Emits the number of indexed photo embeddings — used to gate first-run indexing. */
    fun observeCount(): Flow<Int>
}
