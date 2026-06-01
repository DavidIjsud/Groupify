// feature/personalbum/src/main/.../data/source/LocalDatabasePhotoTextDataSource.kt
package com.palmyrasoft.groupify.feature.personalbum.data.source

import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoTextDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PhotoTextEntity
import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoText
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDatabasePhotoTextDataSource @Inject constructor(
    private val dao: PhotoTextDao,
) {
    suspend fun insertAll(texts: List<PhotoText>) {
        dao.insertAll(texts.map { it.toEntity() })
    }

    suspend fun search(ftsQuery: String): List<String> = dao.search(ftsQuery)

    fun observeCount(): Flow<Int> = dao.countFlow()
}

private fun PhotoText.toEntity(): PhotoTextEntity = PhotoTextEntity(
    photoId = photoId,
    uri = uri,
    text = text,
)
