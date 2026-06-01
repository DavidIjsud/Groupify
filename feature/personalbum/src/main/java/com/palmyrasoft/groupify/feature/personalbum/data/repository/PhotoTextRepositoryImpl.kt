// feature/personalbum/src/main/.../data/repository/PhotoTextRepositoryImpl.kt
package com.palmyrasoft.groupify.feature.personalbum.data.repository

import com.palmyrasoft.groupify.feature.personalbum.data.source.LocalDatabasePhotoTextDataSource
import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoMatch
import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoText
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PhotoTextRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoTextRepositoryImpl @Inject constructor(
    private val dataSource: LocalDatabasePhotoTextDataSource,
) : PhotoTextRepository {

    override suspend fun saveAll(texts: List<PhotoText>) = dataSource.insertAll(texts)

    override suspend fun search(query: String): List<PhotoMatch> {
        val ftsQuery = buildFtsQuery(query) ?: return emptyList()
        return dataSource.search(ftsQuery).map { uri -> PhotoMatch(uri = uri, score = 1f) }
    }

    override fun observeCount(): Flow<Int> = dataSource.observeCount()

    /**
     * Turns a raw user query into a safe FTS4 expression. Splits on non-alphanumerics (stripping
     * any FTS operator characters that would otherwise cause a syntax error) and prefix-matches
     * each token with `*`, so "rece" finds "receipt" and "new york" requires both tokens.
     * Returns null when nothing searchable remains.
     */
    private fun buildFtsQuery(raw: String): String? {
        val tokens = raw.lowercase()
            .split(Regex("[^\\p{L}\\p{N}]+"))
            .filter { it.isNotBlank() }
        if (tokens.isEmpty()) return null
        return tokens.joinToString(" ") { "$it*" }
    }
}
