// feature/personalbum/src/main/.../data/source/LocalDatabaseFaceIndexDataSource.kt
package com.example.groupify.feature.personalbum.data.source

import com.example.groupify.feature.personalbum.data.local.dao.FaceEmbeddingDao
import com.example.groupify.feature.personalbum.data.local.entity.FaceEmbeddingEntity
import com.example.groupify.feature.personalbum.domain.model.BoundingBox
import com.example.groupify.feature.personalbum.domain.model.Face
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDatabaseFaceIndexDataSource @Inject constructor(
    private val faceEmbeddingDao: FaceEmbeddingDao,
) {
    suspend fun insert(face: Face) {
        faceEmbeddingDao.insertAll(listOf(face.toEntity()))
    }

    suspend fun insertAll(faces: List<Face>) {
        faceEmbeddingDao.insertAll(faces.map { it.toEntity() })
    }

    fun queryByPhotoId(photoId: String): Flow<List<Face>> = flow {
        emit(faceEmbeddingDao.getEmbeddingsForPhoto(photoId).map { it.toDomain() })
    }

    fun queryAll(): Flow<List<Face>> =
        faceEmbeddingDao.getAllEmbeddings().map { entities -> entities.map { it.toDomain() } }
}

private fun Face.toEntity(): FaceEmbeddingEntity = FaceEmbeddingEntity(
    photoId = photoId,
    left = boundingBox.left,
    top = boundingBox.top,
    right = boundingBox.right,
    bottom = boundingBox.bottom,
    embedding = embedding.copyOf(),
    createdAt = System.currentTimeMillis(),
)

private fun FaceEmbeddingEntity.toDomain(): Face = Face(
    photoId = photoId,
    boundingBox = BoundingBox(left = left, top = top, right = right, bottom = bottom),
    embedding = embedding.copyOf(),
)
