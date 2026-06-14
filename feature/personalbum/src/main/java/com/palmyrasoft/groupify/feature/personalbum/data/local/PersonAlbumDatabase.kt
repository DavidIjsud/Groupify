// feature/personalbum/src/main/.../data/local/PersonAlbumDatabase.kt
package com.palmyrasoft.groupify.feature.personalbum.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.FaceEmbeddingDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.GroupDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PersonDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoEmbeddingDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoTextDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.FaceEmbeddingEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupPhotoEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PersonEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PhotoEmbeddingEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PhotoEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PhotoTextEntity

@Database(
    entities = [
        PhotoEntity::class,
        FaceEmbeddingEntity::class,
        PersonEntity::class,
        GroupEntity::class,
        GroupPhotoEntity::class,
        PhotoTextEntity::class,
        PhotoEmbeddingEntity::class,
    ],
    version = 6,
    exportSchema = false,
)
abstract class PersonAlbumDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun faceEmbeddingDao(): FaceEmbeddingDao
    abstract fun personDao(): PersonDao
    abstract fun groupDao(): GroupDao
    abstract fun photoTextDao(): PhotoTextDao
    abstract fun photoEmbeddingDao(): PhotoEmbeddingDao
}
