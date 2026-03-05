// feature/personalbum/src/main/.../domain/repository/PhotoRepository.kt
package com.example.groupify.feature.personalbum.domain.repository

import com.example.groupify.feature.personalbum.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getAll(): Flow<List<Photo>>
    suspend fun upsertAll(photos: List<Photo>)
    suspend fun getUnindexed(limit: Int): List<Photo>
    suspend fun markIndexed(photoId: String, timestamp: Long)

    /** Marks multiple photos as indexed in one database round-trip. */
    suspend fun markAllIndexed(photoIds: List<String>, timestamp: Long)

    suspend fun getById(photoId: String): Photo?
    suspend fun getByIds(ids: List<String>): List<Photo>
}
