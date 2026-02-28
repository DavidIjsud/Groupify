// feature/personalbum/src/main/.../data/local/dao/PersonDao.kt
package com.example.groupify.feature.personalbum.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.groupify.feature.personalbum.data.local.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: PersonEntity)

    @Query("SELECT * FROM persons WHERE id = :id")
    fun getById(id: String): Flow<PersonEntity?>

    @Query("SELECT * FROM persons ORDER BY createdAt DESC")
    fun getAll(): Flow<List<PersonEntity>>
}
