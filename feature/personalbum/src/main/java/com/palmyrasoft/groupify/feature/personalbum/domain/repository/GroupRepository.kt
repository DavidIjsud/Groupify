// feature/personalbum/src/main/.../domain/repository/GroupRepository.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.repository

import com.palmyrasoft.groupify.feature.personalbum.domain.model.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    /** All groups, most-recently-updated first. */
    fun getAll(): Flow<List<Group>>

    fun getById(groupId: String): Flow<Group?>

    /** Creates a new group from the given photo URIs and returns it. */
    suspend fun createGroup(name: String, photoUris: List<String>, faceCount: Int): Group

    /**
     * Adds photos to an existing group: dedupes URIs already present, bumps [Group.updatedAt],
     * and raises the stored face count to max(existing, [faceCount]).
     */
    suspend fun addToGroup(groupId: String, photoUris: List<String>, faceCount: Int)

    suspend fun delete(groupId: String)
}
