package com.example.groupify.feature.personalbum.presentation;

import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository;
import com.example.groupify.feature.personalbum.domain.usecase.DetectQueryFacesUseCase;
import com.example.groupify.feature.personalbum.domain.usecase.IndexFacesAndEmbeddingsUseCase;
import com.example.groupify.feature.personalbum.domain.usecase.SearchByPhotoUseCase;
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

  private final Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider;

  private final Provider<DetectQueryFacesUseCase> detectQueryFacesUseCaseProvider;

  private final Provider<FaceIndexRepository> faceIndexRepositoryProvider;

  public PersonAlbumViewModel_Factory(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider,
      Provider<DetectQueryFacesUseCase> detectQueryFacesUseCaseProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider) {
    this.indexFacesAndEmbeddingsUseCaseProvider = indexFacesAndEmbeddingsUseCaseProvider;
    this.searchByPhotoUseCaseProvider = searchByPhotoUseCaseProvider;
    this.detectQueryFacesUseCaseProvider = detectQueryFacesUseCaseProvider;
    this.faceIndexRepositoryProvider = faceIndexRepositoryProvider;
  }

  @Override
  public PersonAlbumViewModel get() {
    return newInstance(indexFacesAndEmbeddingsUseCaseProvider.get(), searchByPhotoUseCaseProvider.get(), detectQueryFacesUseCaseProvider.get(), faceIndexRepositoryProvider.get());
  }

  public static PersonAlbumViewModel_Factory create(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider,
      Provider<DetectQueryFacesUseCase> detectQueryFacesUseCaseProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider) {
    return new PersonAlbumViewModel_Factory(indexFacesAndEmbeddingsUseCaseProvider, searchByPhotoUseCaseProvider, detectQueryFacesUseCaseProvider, faceIndexRepositoryProvider);
  }

  public static PersonAlbumViewModel newInstance(
      IndexFacesAndEmbeddingsUseCase indexFacesAndEmbeddingsUseCase,
      SearchByPhotoUseCase searchByPhotoUseCase, DetectQueryFacesUseCase detectQueryFacesUseCase,
      FaceIndexRepository faceIndexRepository) {
    return new PersonAlbumViewModel(indexFacesAndEmbeddingsUseCase, searchByPhotoUseCase, detectQueryFacesUseCase, faceIndexRepository);
  }
}
