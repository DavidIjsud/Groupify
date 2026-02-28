// feature/personalbum/src/main/.../data/local/PersonAlbumDatabase.kt
package com.example.groupify.feature.personalbum.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.groupify.feature.personalbum.data.local.dao.FaceEmbeddingDao
import com.example.groupify.feature.personalbum.data.local.dao.PhotoDao
import com.example.groupify.feature.personalbum.data.local.entity.FaceEmbeddingEntity
import com.example.groupify.feature.personalbum.data.local.entity.PhotoEntity

@Database(
    entities = [PhotoEntity::class, FaceEmbeddingEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class PersonAlbumDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun faceEmbeddingDao(): FaceEmbeddingDao
}
