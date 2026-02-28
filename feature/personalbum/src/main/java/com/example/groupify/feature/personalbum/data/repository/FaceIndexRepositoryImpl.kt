// feature/personalbum/src/main/.../data/repository/FaceIndexRepositoryImpl.kt
package com.example.groupify.feature.personalbum.data.repository

import com.example.groupify.feature.personalbum.data.source.LocalDatabaseFaceIndexDataSource
import com.example.groupify.feature.personalbum.domain.model.Face
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FaceIndexRepositoryImpl @Inject constructor(
    private val dataSource: LocalDatabaseFaceIndexDataSource,
) : FaceIndexRepository {
    override suspend fun save(face: Face) = dataSource.insert(face)
    override suspend fun saveAll(faces: List<Face>) = dataSource.insertAll(faces)
    override fun getFacesForPhoto(photoId: String): Flow<List<Face>> = dataSource.queryByPhotoId(photoId)
    override fun getAllFaces(): Flow<List<Face>> = dataSource.queryAll()
}
