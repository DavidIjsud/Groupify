// feature/personalbum/src/main/.../data/source/LocalDatabaseFaceIndexDataSource.kt
package com.example.groupify.feature.personalbum.data.source

import com.example.groupify.feature.personalbum.domain.model.Face
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDatabaseFaceIndexDataSource @Inject constructor(
    // TODO: inject RoomDatabase DAO once Room is integrated
) {
    suspend fun insert(face: Face): Unit = TODO("Insert Face entity into Room")
    fun queryByPhotoId(photoId: String): Flow<List<Face>> = TODO("SELECT * FROM faces WHERE photoId = :photoId")
    fun queryAll(): Flow<List<Face>> = TODO("SELECT * FROM faces")
}
