// feature/personalbum/src/main/.../data/repository/PhotoRepositoryImpl.kt
package com.example.groupify.feature.personalbum.data.repository

import com.example.groupify.feature.personalbum.data.source.AndroidMediaStorePhotoDataSource
import com.example.groupify.feature.personalbum.domain.model.Photo
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val dataSource: AndroidMediaStorePhotoDataSource,
) : PhotoRepository {
    override fun getAll(): Flow<List<Photo>> = dataSource.queryPhotos()
}
