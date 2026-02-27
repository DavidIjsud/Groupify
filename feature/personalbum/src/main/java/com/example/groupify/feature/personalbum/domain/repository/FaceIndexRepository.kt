// feature/personalbum/src/main/.../domain/repository/FaceIndexRepository.kt
package com.example.groupify.feature.personalbum.domain.repository

import com.example.groupify.feature.personalbum.domain.model.Face
import kotlinx.coroutines.flow.Flow

interface FaceIndexRepository {
    suspend fun save(face: Face)
    fun getFacesForPhoto(photoId: String): Flow<List<Face>>
    fun getAllFaces(): Flow<List<Face>>
}
