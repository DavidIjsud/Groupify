package com.palmyrasoft.groupify.feature.personalbum.domain.usecase;

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
public final class SearchByTextUseCase_Factory implements Factory<SearchByTextUseCase> {
  private final Provider<PhotoTextRepository> photoTextRepositoryProvider;

  private SearchByTextUseCase_Factory(Provider<PhotoTextRepository> photoTextRepositoryProvider) {
    this.photoTextRepositoryProvider = photoTextRepositoryProvider;
  }

  @Override
  public SearchByTextUseCase get() {
    return newInstance(photoTextRepositoryProvider.get());
  }

  public static SearchByTextUseCase_Factory create(
      Provider<PhotoTextRepository> photoTextRepositoryProvider) {
    return new SearchByTextUseCase_Factory(photoTextRepositoryProvider);
  }

  public static SearchByTextUseCase newInstance(PhotoTextRepository photoTextRepository) {
    return new SearchByTextUseCase(photoTextRepository);
  }
}
