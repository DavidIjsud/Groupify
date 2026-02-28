// feature/personalbum/src/main/.../data/repository/PhotoRepositoryImpl.kt
package com.example.groupify.feature.personalbum.data.repository

import com.example.groupify.feature.personalbum.data.local.dao.PhotoDao
import com.example.groupify.feature.personalbum.data.local.entity.PhotoEntity
import com.example.groupify.feature.personalbum.data.source.AndroidMediaStorePhotoDataSource
import com.example.groupify.feature.personalbum.domain.model.Photo
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val dataSource: AndroidMediaStorePhotoDataSource,
    private val photoDao: PhotoDao,
) : PhotoRepository {

    override fun getAll(): Flow<List<Photo>> = dataSource.queryPhotos()

    override suspend fun upsertAll(photos: List<Photo>) {
        photoDao.upsertPhotos(photos.map { it.toEntity() })
    }

    override suspend fun getUnindexed(limit: Int): List<Photo> =
        photoDao.getUnindexedPhotos(limit).map { it.toDomain() }

    override suspend fun markIndexed(photoId: String, timestamp: Long) {
        photoDao.markPhotoIndexed(photoId, timestamp)
    }

    override suspend fun getById(photoId: String): Photo? =
        photoDao.getPhotoById(photoId)?.toDomain()

    override suspend fun getByIds(ids: List<String>): List<Photo> =
        photoDao.getByIds(ids).map { it.toDomain() }
}

private fun Photo.toEntity(): PhotoEntity = PhotoEntity(
    id = id,
    uri = uri,
    dateTaken = dateTaken,
    lastIndexedAt = null,
)

private fun PhotoEntity.toDomain(): Photo = Photo(
    id = id,
    uri = uri,
    dateTaken = dateTaken,
)
