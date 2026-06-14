package com.palmyrasoft.groupify.feature.personalbum.domain.usecase;

import com.palmyrasoft.groupify.feature.personalbum.domain.detection.FaceDetector;
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.FaceEmbedder;
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.ImageEmbedder;
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.TextRecognizer;
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.DescriptionIndexRepository;
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

  private final Provider<ImageEmbedder> imageEmbedderProvider;

  private final Provider<DescriptionIndexRepository> descriptionIndexRepositoryProvider;

  private IndexFacesAndEmbeddingsUseCase_Factory(Provider<PhotoRepository> photoRepositoryProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<FaceDetector> faceDetectorProvider, Provider<FaceEmbedder> faceEmbedderProvider,
      Provider<TextRecognizer> textRecognizerProvider,
      Provider<PhotoTextRepository> photoTextRepositoryProvider,
      Provider<ImageEmbedder> imageEmbedderProvider,
      Provider<DescriptionIndexRepository> descriptionIndexRepositoryProvider) {
    this.photoRepositoryProvider = photoRepositoryProvider;
    this.faceIndexRepositoryProvider = faceIndexRepositoryProvider;
    this.faceDetectorProvider = faceDetectorProvider;
    this.faceEmbedderProvider = faceEmbedderProvider;
    this.textRecognizerProvider = textRecognizerProvider;
    this.photoTextRepositoryProvider = photoTextRepositoryProvider;
    this.imageEmbedderProvider = imageEmbedderProvider;
    this.descriptionIndexRepositoryProvider = descriptionIndexRepositoryProvider;
  }

  @Override
  public IndexFacesAndEmbeddingsUseCase get() {
    return newInstance(photoRepositoryProvider.get(), faceIndexRepositoryProvider.get(), faceDetectorProvider.get(), faceEmbedderProvider.get(), textRecognizerProvider.get(), photoTextRepositoryProvider.get(), imageEmbedderProvider.get(), descriptionIndexRepositoryProvider.get());
  }

  public static IndexFacesAndEmbeddingsUseCase_Factory create(
      Provider<PhotoRepository> photoRepositoryProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<FaceDetector> faceDetectorProvider, Provider<FaceEmbedder> faceEmbedderProvider,
      Provider<TextRecognizer> textRecognizerProvider,
      Provider<PhotoTextRepository> photoTextRepositoryProvider,
      Provider<ImageEmbedder> imageEmbedderProvider,
      Provider<DescriptionIndexRepository> descriptionIndexRepositoryProvider) {
    return new IndexFacesAndEmbeddingsUseCase_Factory(photoRepositoryProvider, faceIndexRepositoryProvider, faceDetectorProvider, faceEmbedderProvider, textRecognizerProvider, photoTextRepositoryProvider, imageEmbedderProvider, descriptionIndexRepositoryProvider);
  }

  public static IndexFacesAndEmbeddingsUseCase newInstance(PhotoRepository photoRepository,
      FaceIndexRepository faceIndexRepository, FaceDetector faceDetector, FaceEmbedder faceEmbedder,
      TextRecognizer textRecognizer, PhotoTextRepository photoTextRepository,
      ImageEmbedder imageEmbedder, DescriptionIndexRepository descriptionIndexRepository) {
    return new IndexFacesAndEmbeddingsUseCase(photoRepository, faceIndexRepository, faceDetector, faceEmbedder, textRecognizer, photoTextRepository, imageEmbedder, descriptionIndexRepository);
  }
}
