package com.example.groupify.feature.personalbum.domain.usecase;

import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository;
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository;
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
public final class GetPersonAlbumUseCase_Factory implements Factory<GetPersonAlbumUseCase> {
  private final Provider<PersonRepository> personRepositoryProvider;

  private final Provider<FaceIndexRepository> faceIndexRepositoryProvider;

  private final Provider<PhotoRepository> photoRepositoryProvider;

  public GetPersonAlbumUseCase_Factory(Provider<PersonRepository> personRepositoryProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<PhotoRepository> photoRepositoryProvider) {
    this.personRepositoryProvider = personRepositoryProvider;
    this.faceIndexRepositoryProvider = faceIndexRepositoryProvider;
    this.photoRepositoryProvider = photoRepositoryProvider;
  }

  @Override
  public GetPersonAlbumUseCase get() {
    return newInstance(personRepositoryProvider.get(), faceIndexRepositoryProvider.get(), photoRepositoryProvider.get());
  }

  public static GetPersonAlbumUseCase_Factory create(
      Provider<PersonRepository> personRepositoryProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<PhotoRepository> photoRepositoryProvider) {
    return new GetPersonAlbumUseCase_Factory(personRepositoryProvider, faceIndexRepositoryProvider, photoRepositoryProvider);
  }

  public static GetPersonAlbumUseCase newInstance(PersonRepository personRepository,
      FaceIndexRepository faceIndexRepository, PhotoRepository photoRepository) {
    return new GetPersonAlbumUseCase(personRepository, faceIndexRepository, photoRepository);
  }
}
