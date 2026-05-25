// feature/personalbum/src/main/.../data/repository/GroupRepositoryImpl.kt
package com.palmyrasoft.groupify.feature.personalbum.data.repository

import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.GroupDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupPhotoEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.relation.GroupWithPhotos
import com.palmyrasoft.groupify.feature.personalbum.domain.model.Group
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao,
) : GroupRepository {

    override fun getAll(): Flow<List<Group>> =
        groupDao.getGroupsWithPhotos().map { rows -> rows.map { it.toDomain() } }

    override fun getById(groupId: String): Flow<Group?> =
        groupDao.getGroupWithPhotos(groupId).map { it?.toDomain() }

    override suspend fun createGroup(name: String, photoUris: List<String>, faceCount: Int): Group {
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        groupDao.insertGroup(
            GroupEntity(id = id, name = name, faceCount = faceCount, createdAt = now, updatedAt = now),
        )
        groupDao.insertPhotos(photoUris.toPhotoEntities(id, now))
        return groupDao.getGroupWithPhotosOnce(id)?.toDomain()
            ?: Group(id, name, photoUris, faceCount, now, now)
    }

    override suspend fun addToGroup(groupId: String, photoUris: List<String>, faceCount: Int) {
        val now = System.currentTimeMillis()
        groupDao.insertPhotos(photoUris.toPhotoEntities(groupId, now)) // IGNORE dedupes
        groupDao.touchGroup(groupId, updatedAt = now, faceCount = faceCount)
    }

    override suspend fun delete(groupId: String) = groupDao.deleteGroup(groupId)
}

private fun List<String>.toPhotoEntities(groupId: String, addedAt: Long): List<GroupPhotoEntity> =
    map { uri -> GroupPhotoEntity(groupId = groupId, photoUri = uri, addedAt = addedAt) }

private fun GroupWithPhotos.toDomain(): Group = Group(
    id = group.id,
    name = group.name,
    // Newest-added first so collages/detail show the most recent additions.
    photoUris = photos.sortedByDescending { it.addedAt }.map { it.photoUri },
    faceCount = group.faceCount,
    createdAt = group.createdAt,
    updatedAt = group.updatedAt,
)
