package com.palmyrasoft.groupify.feature.personalbum.data.repository;

import com.palmyrasoft.groupify.feature.personalbum.data.source.LocalDatabasePhotoEmbeddingDataSource;
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
public final class DescriptionIndexRepositoryImpl_Factory implements Factory<DescriptionIndexRepositoryImpl> {
  private final Provider<LocalDatabasePhotoEmbeddingDataSource> dataSourceProvider;

  private DescriptionIndexRepositoryImpl_Factory(
      Provider<LocalDatabasePhotoEmbeddingDataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public DescriptionIndexRepositoryImpl get() {
    return newInstance(dataSourceProvider.get());
  }

  public static DescriptionIndexRepositoryImpl_Factory create(
      Provider<LocalDatabasePhotoEmbeddingDataSource> dataSourceProvider) {
    return new DescriptionIndexRepositoryImpl_Factory(dataSourceProvider);
  }

  public static DescriptionIndexRepositoryImpl newInstance(
      LocalDatabasePhotoEmbeddingDataSource dataSource) {
    return new DescriptionIndexRepositoryImpl(dataSource);
  }
}
