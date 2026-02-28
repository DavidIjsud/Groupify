package com.example.groupify.feature.personalbum.presentation;

import com.example.groupify.feature.personalbum.domain.repository.PersonRepository;
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository;
import com.example.groupify.feature.personalbum.domain.usecase.CreatePersonAlbumUseCase;
import com.example.groupify.feature.personalbum.domain.usecase.DetectFacesInPhotoUseCase;
import com.example.groupify.feature.personalbum.domain.usecase.FindMatchingPhotosUseCase;
import com.example.groupify.feature.personalbum.domain.usecase.GetPersonAlbumUseCase;
import com.example.groupify.feature.personalbum.domain.usecase.IndexFacesAndEmbeddingsUseCase;
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
public final class PersonAlbumViewModel_Factory implements Factory<PersonAlbumViewModel> {
  private final Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider;

  private final Provider<CreatePersonAlbumUseCase> createPersonAlbumUseCaseProvider;

  private final Provider<DetectFacesInPhotoUseCase> detectFacesInPhotoUseCaseProvider;

  private final Provider<FindMatchingPhotosUseCase> findMatchingPhotosUseCaseProvider;

  private final Provider<GetPersonAlbumUseCase> getPersonAlbumUseCaseProvider;

  private final Provider<PhotoRepository> photoRepositoryProvider;

  private final Provider<PersonRepository> personRepositoryProvider;

  public PersonAlbumViewModel_Factory(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<CreatePersonAlbumUseCase> createPersonAlbumUseCaseProvider,
      Provider<DetectFacesInPhotoUseCase> detectFacesInPhotoUseCaseProvider,
      Provider<FindMatchingPhotosUseCase> findMatchingPhotosUseCaseProvider,
      Provider<GetPersonAlbumUseCase> getPersonAlbumUseCaseProvider,
      Provider<PhotoRepository> photoRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider) {
    this.indexFacesAndEmbeddingsUseCaseProvider = indexFacesAndEmbeddingsUseCaseProvider;
    this.createPersonAlbumUseCaseProvider = createPersonAlbumUseCaseProvider;
    this.detectFacesInPhotoUseCaseProvider = detectFacesInPhotoUseCaseProvider;
    this.findMatchingPhotosUseCaseProvider = findMatchingPhotosUseCaseProvider;
    this.getPersonAlbumUseCaseProvider = getPersonAlbumUseCaseProvider;
    this.photoRepositoryProvider = photoRepositoryProvider;
    this.personRepositoryProvider = personRepositoryProvider;
  }

  @Override
  public PersonAlbumViewModel get() {
    return newInstance(indexFacesAndEmbeddingsUseCaseProvider.get(), createPersonAlbumUseCaseProvider.get(), detectFacesInPhotoUseCaseProvider.get(), findMatchingPhotosUseCaseProvider.get(), getPersonAlbumUseCaseProvider.get(), photoRepositoryProvider.get(), personRepositoryProvider.get());
  }

  public static PersonAlbumViewModel_Factory create(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<CreatePersonAlbumUseCase> createPersonAlbumUseCaseProvider,
      Provider<DetectFacesInPhotoUseCase> detectFacesInPhotoUseCaseProvider,
      Provider<FindMatchingPhotosUseCase> findMatchingPhotosUseCaseProvider,
      Provider<GetPersonAlbumUseCase> getPersonAlbumUseCaseProvider,
      Provider<PhotoRepository> photoRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider) {
    return new PersonAlbumViewModel_Factory(indexFacesAndEmbeddingsUseCaseProvider, createPersonAlbumUseCaseProvider, detectFacesInPhotoUseCaseProvider, findMatchingPhotosUseCaseProvider, getPersonAlbumUseCaseProvider, photoRepositoryProvider, personRepositoryProvider);
  }

  public static PersonAlbumViewModel newInstance(
      IndexFacesAndEmbeddingsUseCase indexFacesAndEmbeddingsUseCase,
      CreatePersonAlbumUseCase createPersonAlbumUseCase,
      DetectFacesInPhotoUseCase detectFacesInPhotoUseCase,
      FindMatchingPhotosUseCase findMatchingPhotosUseCase,
      GetPersonAlbumUseCase getPersonAlbumUseCase, PhotoRepository photoRepository,
      PersonRepository personRepository) {
    return new PersonAlbumViewModel(indexFacesAndEmbeddingsUseCase, createPersonAlbumUseCase, detectFacesInPhotoUseCase, findMatchingPhotosUseCase, getPersonAlbumUseCase, photoRepository, personRepository);
  }
}
