// feature/personalbum/src/main/.../domain/repository/PhotoTextRepository.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.repository

import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoMatch
import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoText
import kotlinx.coroutines.flow.Flow

/**
 * On-device index of text extracted from gallery photos. Sibling of
 * [com.palmyrasoft.groupify.feature.personalbum.domain.repository.FaceIndexRepository] for the
 * text-search flow.
 */
interface PhotoTextRepository {
    suspend fun saveAll(texts: List<PhotoText>)

    /** Full-text search; returns matching photos (most relevant first). */
    suspend fun search(query: String): List<PhotoMatch>

    /** Emits the number of indexed photos that have text — used to gate first-run indexing. */
    fun observeCount(): Flow<Int>
}
