// feature/personalbum/src/main/.../data/local/PersonAlbumDatabase.kt
package com.example.groupify.feature.personalbum.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.groupify.feature.personalbum.data.local.dao.FaceEmbeddingDao
import com.example.groupify.feature.personalbum.data.local.dao.PersonDao
import com.example.groupify.feature.personalbum.data.local.dao.PhotoDao
import com.example.groupify.feature.personalbum.data.local.entity.FaceEmbeddingEntity
import com.example.groupify.feature.personalbum.data.local.entity.PersonEntity
import com.example.groupify.feature.personalbum.data.local.entity.PhotoEntity

@Database(
    entities = [PhotoEntity::class, FaceEmbeddingEntity::class, PersonEntity::class],
    version = 3,
    exportSchema = false,
)
abstract class PersonAlbumDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun faceEmbeddingDao(): FaceEmbeddingDao
    abstract fun personDao(): PersonDao
}
