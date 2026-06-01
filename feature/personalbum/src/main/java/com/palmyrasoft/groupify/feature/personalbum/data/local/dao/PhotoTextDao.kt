// feature/personalbum/src/main/.../data/local/dao/PhotoTextDao.kt
package com.palmyrasoft.groupify.feature.personalbum.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PhotoTextEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoTextDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PhotoTextEntity>)

    /**
     * FTS MATCH against the `text` column only (so the query never matches the stored uri/photoId).
     * [ftsQuery] is an FTS4 expression built by the repository, e.g. `receipt* new*`.
     */
    @Query("SELECT uri FROM photo_texts WHERE text MATCH :ftsQuery")
    suspend fun search(ftsQuery: String): List<String>

    @Query("SELECT COUNT(*) FROM photo_texts")
    fun countFlow(): Flow<Int>
}
