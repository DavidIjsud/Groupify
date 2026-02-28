// feature/personalbum/src/main/.../di/DatabaseModule.kt
package com.example.groupify.feature.personalbum.di

import android.content.Context
import androidx.room.Room
import com.example.groupify.feature.personalbum.data.local.PersonAlbumDatabase
import com.example.groupify.feature.personalbum.data.local.dao.FaceEmbeddingDao
import com.example.groupify.feature.personalbum.data.local.dao.PhotoDao
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
        ).build()

    @Provides
    fun providePhotoDao(db: PersonAlbumDatabase): PhotoDao = db.photoDao()

    @Provides
    fun provideFaceEmbeddingDao(db: PersonAlbumDatabase): FaceEmbeddingDao = db.faceEmbeddingDao()
}
