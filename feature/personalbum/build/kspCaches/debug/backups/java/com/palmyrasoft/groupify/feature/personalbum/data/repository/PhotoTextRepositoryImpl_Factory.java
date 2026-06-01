package com.palmyrasoft.groupify.feature.personalbum.data.repository;

import com.palmyrasoft.groupify.feature.personalbum.data.source.LocalDatabasePhotoTextDataSource;
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
public final class PhotoTextRepositoryImpl_Factory implements Factory<PhotoTextRepositoryImpl> {
  private final Provider<LocalDatabasePhotoTextDataSource> dataSourceProvider;

  private PhotoTextRepositoryImpl_Factory(
      Provider<LocalDatabasePhotoTextDataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public PhotoTextRepositoryImpl get() {
    return newInstance(dataSourceProvider.get());
  }

  public static PhotoTextRepositoryImpl_Factory create(
      Provider<LocalDatabasePhotoTextDataSource> dataSourceProvider) {
    return new PhotoTextRepositoryImpl_Factory(dataSourceProvider);
  }

  public static PhotoTextRepositoryImpl newInstance(LocalDatabasePhotoTextDataSource dataSource) {
    return new PhotoTextRepositoryImpl(dataSource);
  }
}
