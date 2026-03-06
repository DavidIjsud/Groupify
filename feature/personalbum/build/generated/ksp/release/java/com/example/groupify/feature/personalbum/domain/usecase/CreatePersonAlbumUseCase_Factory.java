package com.example.groupify.feature.personalbum.domain.usecase;

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector;
import com.example.groupify.feature.personalbum.domain.recognition.FaceEmbedder;
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository;
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
public final class CreatePersonAlbumUseCase_Factory implements Factory<CreatePersonAlbumUseCase> {
  private final Provider<PersonRepository> personRepositoryProvider;

  private final Provider<FaceDetector> faceDetectorProvider;

  private final Provider<FaceEmbedder> faceEmbedderProvider;

  public CreatePersonAlbumUseCase_Factory(Provider<PersonRepository> personRepositoryProvider,
      Provider<FaceDetector> faceDetectorProvider, Provider<FaceEmbedder> faceEmbedderProvider) {
    this.personRepositoryProvider = personRepositoryProvider;
    this.faceDetectorProvider = faceDetectorProvider;
    this.faceEmbedderProvider = faceEmbedderProvider;
  }

  @Override
  public CreatePersonAlbumUseCase get() {
    return newInstance(personRepositoryProvider.get(), faceDetectorProvider.get(), faceEmbedderProvider.get());
  }

  public static CreatePersonAlbumUseCase_Factory create(
      Provider<PersonRepository> personRepositoryProvider,
      Provider<FaceDetector> faceDetectorProvider, Provider<FaceEmbedder> faceEmbedderProvider) {
    return new CreatePersonAlbumUseCase_Factory(personRepositoryProvider, faceDetectorProvider, faceEmbedderProvider);
  }

  public static CreatePersonAlbumUseCase newInstance(PersonRepository personRepository,
      FaceDetector faceDetector, FaceEmbedder faceEmbedder) {
    return new CreatePersonAlbumUseCase(personRepository, faceDetector, faceEmbedder);
  }
}
