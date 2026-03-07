// feature/personalbum/src/main/.../di/PersonAlbumModule.kt
package com.palmyrasoft.groupify.feature.personalbum.di

import com.palmyrasoft.groupify.feature.personalbum.data.ml.MlKitFaceDetector
import com.palmyrasoft.groupify.feature.personalbum.data.ml.TFLiteFaceNetEmbedder
import com.palmyrasoft.groupify.feature.personalbum.data.repository.FaceIndexRepositoryImpl
import com.palmyrasoft.groupify.feature.personalbum.data.repository.PersonRepositoryImpl
import com.palmyrasoft.groupify.feature.personalbum.data.repository.PhotoRepositoryImpl
import com.palmyrasoft.groupify.feature.personalbum.data.thumbnail.AndroidQueryFaceThumbnailGenerator
import com.palmyrasoft.groupify.feature.personalbum.domain.detection.FaceDetector
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PersonRepository
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PhotoRepository
import com.palmyrasoft.groupify.feature.personalbum.domain.thumbnail.QueryFaceThumbnailGenerator
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

    @Binds
    @Singleton
    abstract fun bindFaceEmbedder(impl: TFLiteFaceNetEmbedder): FaceEmbedder

    @Binds
    @Singleton
    abstract fun bindQueryFaceThumbnailGenerator(
        impl: AndroidQueryFaceThumbnailGenerator,
    ): QueryFaceThumbnailGenerator
}
