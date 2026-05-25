// feature/personalbum/src/main/.../data/local/dao/GroupDao.kt
package com.palmyrasoft.groupify.feature.personalbum.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupPhotoEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.relation.GroupWithPhotos
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    /** IGNORE drops duplicate (groupId, photoUri) rows, giving "add to group" free dedupe. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhotos(photos: List<GroupPhotoEntity>)

    /** Bumps updatedAt and raises the stored face count to max(existing, :faceCount). */
    @Query("UPDATE groups SET updatedAt = :updatedAt, faceCount = MAX(faceCount, :faceCount) WHERE id = :groupId")
    suspend fun touchGroup(groupId: String, updatedAt: Long, faceCount: Int)

    @Query("DELETE FROM groups WHERE id = :groupId")
    suspend fun deleteGroup(groupId: String)

    @Transaction
    @Query("SELECT * FROM groups ORDER BY updatedAt DESC")
    fun getGroupsWithPhotos(): Flow<List<GroupWithPhotos>>

    @Transaction
    @Query("SELECT * FROM groups WHERE id = :groupId")
    fun getGroupWithPhotos(groupId: String): Flow<GroupWithPhotos?>

    /** Convenience used by createGroup so it can return a fully-formed domain model. */
    @Transaction
    @Query("SELECT * FROM groups WHERE id = :groupId")
    suspend fun getGroupWithPhotosOnce(groupId: String): GroupWithPhotos?
}
