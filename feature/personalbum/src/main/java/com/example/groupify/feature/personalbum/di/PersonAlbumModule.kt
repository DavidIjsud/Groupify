// feature/personalbum/src/main/.../di/PersonAlbumModule.kt
package com.example.groupify.feature.personalbum.di

import com.example.groupify.feature.personalbum.data.ml.MlKitFaceDetector
import com.example.groupify.feature.personalbum.data.repository.FaceIndexRepositoryImpl
import com.example.groupify.feature.personalbum.data.repository.PersonRepositoryImpl
import com.example.groupify.feature.personalbum.data.repository.PhotoRepositoryImpl
import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PersonAlbumModule {

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(impl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    @Singleton
    abstract fun bindFaceIndexRepository(impl: FaceIndexRepositoryImpl): FaceIndexRepository

    @Binds
    @Singleton
    abstract fun bindPersonRepository(impl: PersonRepositoryImpl): PersonRepository

    @Binds
    @Singleton
    abstract fun bindFaceDetector(impl: MlKitFaceDetector): FaceDetector
}
