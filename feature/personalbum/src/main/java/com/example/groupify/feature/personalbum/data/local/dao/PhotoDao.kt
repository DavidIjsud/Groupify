// feature/personalbum/src/main/.../data/local/dao/PhotoDao.kt
package com.example.groupify.feature.personalbum.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.groupify.feature.personalbum.data.local.entity.PhotoEntity

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertPhotos(photos: List<PhotoEntity>)

    @Query("UPDATE photos SET lastIndexedAt = :timestamp WHERE id = :photoId")
    suspend fun markPhotoIndexed(photoId: String, timestamp: Long)

    @Query("SELECT * FROM photos WHERE lastIndexedAt IS NULL LIMIT :limit")
    suspend fun getUnindexedPhotos(limit: Int): List<PhotoEntity>

    @Query("SELECT * FROM photos WHERE id = :photoId")
    suspend fun getPhotoById(photoId: String): PhotoEntity?
}
