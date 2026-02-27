// feature/personalbum/src/main/.../domain/repository/PhotoRepository.kt
package com.example.groupify.feature.personalbum.domain.repository

import com.example.groupify.feature.personalbum.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getAll(): Flow<List<Photo>>
}
