// feature/personalbum/src/main/.../di/DatabaseModule.kt
package com.palmyrasoft.groupify.feature.personalbum.di

import android.content.Context
import androidx.room.Room
import com.palmyrasoft.groupify.feature.personalbum.data.local.PersonAlbumDatabase
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.FaceEmbeddingDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.GroupDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PersonDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoEmbeddingDao
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoTextDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PersonAlbumDatabase =
        Room.databaseBuilder(
            context,
            PersonAlbumDatabase::class.java,
            "person_album.db",
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providePhotoDao(db: PersonAlbumDatabase): PhotoDao = db.photoDao()

    @Provides
    fun provideFaceEmbeddingDao(db: PersonAlbumDatabase): FaceEmbeddingDao = db.faceEmbeddingDao()

    @Provides
    fun providePersonDao(db: PersonAlbumDatabase): PersonDao = db.personDao()

    @Provides
    fun provideGroupDao(db: PersonAlbumDatabase): GroupDao = db.groupDao()

    @Provides
    fun providePhotoTextDao(db: PersonAlbumDatabase): PhotoTextDao = db.photoTextDao()

    @Provides
    fun providePhotoEmbeddingDao(db: PersonAlbumDatabase): PhotoEmbeddingDao = db.photoEmbeddingDao()
}
