package com.palmyrasoft.groupify.feature.personalbum.data.repository;

import com.palmyrasoft.groupify.feature.personalbum.data.source.LocalDatabaseFaceIndexDataSource;
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
public final class FaceIndexRepositoryImpl_Factory implements Factory<FaceIndexRepositoryImpl> {
  private final Provider<LocalDatabaseFaceIndexDataSource> dataSourceProvider;

  private FaceIndexRepositoryImpl_Factory(
      Provider<LocalDatabaseFaceIndexDataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public FaceIndexRepositoryImpl get() {
    return newInstance(dataSourceProvider.get());
  }

  public static FaceIndexRepositoryImpl_Factory create(
      Provider<LocalDatabaseFaceIndexDataSource> dataSourceProvider) {
    return new FaceIndexRepositoryImpl_Factory(dataSourceProvider);
  }

  public static FaceIndexRepositoryImpl newInstance(LocalDatabaseFaceIndexDataSource dataSource) {
    return new FaceIndexRepositoryImpl(dataSource);
  }
}
