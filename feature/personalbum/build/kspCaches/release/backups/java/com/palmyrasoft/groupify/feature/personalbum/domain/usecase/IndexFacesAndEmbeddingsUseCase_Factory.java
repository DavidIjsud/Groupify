package com.palmyrasoft.groupify.feature.personalbum.domain.usecase;

import com.palmyrasoft.groupify.feature.personalbum.domain.detection.FaceDetector;
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.FaceEmbedder;
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.TextRecognizer;
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.FaceIndexRepository;
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PhotoRepository;
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PhotoTextRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class IndexFacesAndEmbeddingsUseCase_Factory implements Factory<IndexFacesAndEmbeddingsUseCase> {
  private final Provider<PhotoRepository> photoRepositoryProvider;

  private final Provider<FaceIndexRepository> faceIndexRepositoryProvider;

  private final Provider<FaceDetector> faceDetectorProvider;

  private final Provider<FaceEmbedder> faceEmbedderProvider;

  private final Provider<TextRecognizer> textRecognizerProvider;

  private final Provider<PhotoTextRepository> photoTextRepositoryProvider;

  private IndexFacesAndEmbeddingsUseCase_Factory(Provider<PhotoRepository> photoRepositoryProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<FaceDetector> faceDetectorProvider, Provider<FaceEmbedder> faceEmbedderProvider,
      Provider<TextRecognizer> textRecognizerProvider,
      Provider<PhotoTextRepository> photoTextRepositoryProvider) {
    this.photoRepositoryProvider = photoRepositoryProvider;
    this.faceIndexRepositoryProvider = faceIndexRepositoryProvider;
    this.faceDetectorProvider = faceDetectorProvider;
    this.faceEmbedderProvider = faceEmbedderProvider;
    this.textRecognizerProvider = textRecognizerProvider;
    this.photoTextRepositoryProvider = photoTextRepositoryProvider;
  }

  @Override
  public IndexFacesAndEmbeddingsUseCase get() {
    return newInstance(photoRepositoryProvider.get(), faceIndexRepositoryProvider.get(), faceDetectorProvider.get(), faceEmbedderProvider.get(), textRecognizerProvider.get(), photoTextRepositoryProvider.get());
  }

  public static IndexFacesAndEmbeddingsUseCase_Factory create(
      Provider<PhotoRepository> photoRepositoryProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<FaceDetector> faceDetectorProvider, Provider<FaceEmbedder> faceEmbedderProvider,
      Provider<TextRecognizer> textRecognizerProvider,
      Provider<PhotoTextRepository> photoTextRepositoryProvider) {
    return new IndexFacesAndEmbeddingsUseCase_Factory(photoRepositoryProvider, faceIndexRepositoryProvider, faceDetectorProvider, faceEmbedderProvider, textRecognizerProvider, photoTextRepositoryProvider);
  }

  public static IndexFacesAndEmbeddingsUseCase newInstance(PhotoRepository photoRepository,
      FaceIndexRepository faceIndexRepository, FaceDetector faceDetector, FaceEmbedder faceEmbedder,
      TextRecognizer textRecognizer, PhotoTextRepository photoTextRepository) {
    return new IndexFacesAndEmbeddingsUseCase(photoRepository, faceIndexRepository, faceDetector, faceEmbedder, textRecognizer, photoTextRepository);
  }
}
