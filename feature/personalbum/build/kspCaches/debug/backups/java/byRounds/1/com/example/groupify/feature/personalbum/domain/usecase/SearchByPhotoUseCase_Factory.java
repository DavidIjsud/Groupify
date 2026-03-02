package com.example.groupify.feature.personalbum.domain.usecase;

import com.example.groupify.feature.personalbum.domain.recognition.FaceEmbedder;
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository;
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class SearchByPhotoUseCase_Factory implements Factory<SearchByPhotoUseCase> {
  private final Provider<FaceEmbedder> faceEmbedderProvider;

  private final Provider<FaceIndexRepository> faceIndexRepositoryProvider;

  private final Provider<PhotoRepository> photoRepositoryProvider;

  public SearchByPhotoUseCase_Factory(Provider<FaceEmbedder> faceEmbedderProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<PhotoRepository> photoRepositoryProvider) {
    this.faceEmbedderProvider = faceEmbedderProvider;
    this.faceIndexRepositoryProvider = faceIndexRepositoryProvider;
    this.photoRepositoryProvider = photoRepositoryProvider;
  }

  @Override
  public SearchByPhotoUseCase get() {
    return newInstance(faceEmbedderProvider.get(), faceIndexRepositoryProvider.get(), photoRepositoryProvider.get());
  }

  public static SearchByPhotoUseCase_Factory create(Provider<FaceEmbedder> faceEmbedderProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<PhotoRepository> photoRepositoryProvider) {
    return new SearchByPhotoUseCase_Factory(faceEmbedderProvider, faceIndexRepositoryProvider, photoRepositoryProvider);
  }

  public static SearchByPhotoUseCase newInstance(FaceEmbedder faceEmbedder,
      FaceIndexRepository faceIndexRepository, PhotoRepository photoRepository) {
    return new SearchByPhotoUseCase(faceEmbedder, faceIndexRepository, photoRepository);
  }
}
