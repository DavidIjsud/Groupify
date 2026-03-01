package com.example.groupify.feature.personalbum.presentation;

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

  public PersonAlbumViewModel_Factory(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider) {
    this.indexFacesAndEmbeddingsUseCaseProvider = indexFacesAndEmbeddingsUseCaseProvider;
    this.searchByPhotoUseCaseProvider = searchByPhotoUseCaseProvider;
  }

  @Override
  public PersonAlbumViewModel get() {
    return newInstance(indexFacesAndEmbeddingsUseCaseProvider.get(), searchByPhotoUseCaseProvider.get());
  }

  public static PersonAlbumViewModel_Factory create(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider) {
    return new PersonAlbumViewModel_Factory(indexFacesAndEmbeddingsUseCaseProvider, searchByPhotoUseCaseProvider);
  }

  public static PersonAlbumViewModel newInstance(
      IndexFacesAndEmbeddingsUseCase indexFacesAndEmbeddingsUseCase,
      SearchByPhotoUseCase searchByPhotoUseCase) {
    return new PersonAlbumViewModel(indexFacesAndEmbeddingsUseCase, searchByPhotoUseCase);
  }
}
