package com.example.groupify.feature.personalbum.data.repository;

import com.example.groupify.feature.personalbum.data.source.LocalDatabaseFaceIndexDataSource;
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
public final class FaceIndexRepositoryImpl_Factory implements Factory<FaceIndexRepositoryImpl> {
  private final Provider<LocalDatabaseFaceIndexDataSource> dataSourceProvider;

  public FaceIndexRepositoryImpl_Factory(
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
