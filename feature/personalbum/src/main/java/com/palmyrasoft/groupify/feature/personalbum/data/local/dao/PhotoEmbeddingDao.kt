// feature/personalbum/src/main/.../data/local/dao/PhotoEmbeddingDao.kt
package com.palmyrasoft.groupify.feature.personalbum.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PhotoEmbeddingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoEmbeddingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(embeddings: List<PhotoEmbeddingEntity>)

    @Query("SELECT * FROM photo_embeddings")
    fun getAllEmbeddings(): Flow<List<PhotoEmbeddingEntity>>

    @Query("SELECT COUNT(*) FROM photo_embeddings")
    fun countFlow(): Flow<Int>
}
