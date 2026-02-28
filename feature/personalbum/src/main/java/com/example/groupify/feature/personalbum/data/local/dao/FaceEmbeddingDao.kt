// feature/personalbum/src/main/.../data/local/dao/FaceEmbeddingDao.kt
package com.example.groupify.feature.personalbum.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.groupify.feature.personalbum.data.local.entity.FaceEmbeddingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FaceEmbeddingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(embeddings: List<FaceEmbeddingEntity>)

    @Query("SELECT * FROM face_embeddings")
    fun getAllEmbeddings(): Flow<List<FaceEmbeddingEntity>>

    @Query("SELECT * FROM face_embeddings WHERE photoId = :photoId")
    suspend fun getEmbeddingsForPhoto(photoId: String): List<FaceEmbeddingEntity>

    @Query("SELECT * FROM face_embeddings WHERE photoId IN (:photoIds)")
    suspend fun getEmbeddingsForPhotoIds(photoIds: List<String>): List<FaceEmbeddingEntity>
}
