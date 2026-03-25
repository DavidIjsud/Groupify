package com.palmyrasoft.groupify.feature.personalbum.domain.usecase;

import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PhotoRepository;
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
public final class IndexPhotosUseCase_Factory implements Factory<IndexPhotosUseCase> {
  private final Provider<PhotoRepository> photoRepositoryProvider;

  private IndexPhotosUseCase_Factory(Provider<PhotoRepository> photoRepositoryProvider) {
    this.photoRepositoryProvider = photoRepositoryProvider;
  }

  @Override
  public IndexPhotosUseCase get() {
    return newInstance(photoRepositoryProvider.get());
  }

  public static IndexPhotosUseCase_Factory create(
      Provider<PhotoRepository> photoRepositoryProvider) {
    return new IndexPhotosUseCase_Factory(photoRepositoryProvider);
  }

  public static IndexPhotosUseCase newInstance(PhotoRepository photoRepository) {
    return new IndexPhotosUseCase(photoRepository);
  }
}
