package com.example.groupify.feature.personalbum.domain.usecase;

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector;
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
public final class FindMatchingPhotosUseCase_Factory implements Factory<FindMatchingPhotosUseCase> {
  private final Provider<PhotoRepository> photoRepositoryProvider;

  private final Provider<FaceIndexRepository> faceIndexRepositoryProvider;

  private final Provider<FaceDetector> faceDetectorProvider;

  private final Provider<FaceEmbedder> faceEmbedderProvider;

  public FindMatchingPhotosUseCase_Factory(Provider<PhotoRepository> photoRepositoryProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<FaceDetector> faceDetectorProvider, Provider<FaceEmbedder> faceEmbedderProvider) {
    this.photoRepositoryProvider = photoRepositoryProvider;
    this.faceIndexRepositoryProvider = faceIndexRepositoryProvider;
    this.faceDetectorProvider = faceDetectorProvider;
    this.faceEmbedderProvider = faceEmbedderProvider;
  }

  @Override
  public FindMatchingPhotosUseCase get() {
    return newInstance(photoRepositoryProvider.get(), faceIndexRepositoryProvider.get(), faceDetectorProvider.get(), faceEmbedderProvider.get());
  }

  public static FindMatchingPhotosUseCase_Factory create(
      Provider<PhotoRepository> photoRepositoryProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<FaceDetector> faceDetectorProvider, Provider<FaceEmbedder> faceEmbedderProvider) {
    return new FindMatchingPhotosUseCase_Factory(photoRepositoryProvider, faceIndexRepositoryProvider, faceDetectorProvider, faceEmbedderProvider);
  }

  public static FindMatchingPhotosUseCase newInstance(PhotoRepository photoRepository,
      FaceIndexRepository faceIndexRepository, FaceDetector faceDetector,
      FaceEmbedder faceEmbedder) {
    return new FindMatchingPhotosUseCase(photoRepository, faceIndexRepository, faceDetector, faceEmbedder);
  }
}
