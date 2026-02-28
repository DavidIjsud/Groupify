package com.example.groupify.feature.personalbum.data.source;

import com.example.groupify.feature.personalbum.data.local.dao.FaceEmbeddingDao;
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
public final class LocalDatabaseFaceIndexDataSource_Factory implements Factory<LocalDatabaseFaceIndexDataSource> {
  private final Provider<FaceEmbeddingDao> faceEmbeddingDaoProvider;

  public LocalDatabaseFaceIndexDataSource_Factory(
      Provider<FaceEmbeddingDao> faceEmbeddingDaoProvider) {
    this.faceEmbeddingDaoProvider = faceEmbeddingDaoProvider;
  }

  @Override
  public LocalDatabaseFaceIndexDataSource get() {
    return newInstance(faceEmbeddingDaoProvider.get());
  }

  public static LocalDatabaseFaceIndexDataSource_Factory create(
      Provider<FaceEmbeddingDao> faceEmbeddingDaoProvider) {
    return new LocalDatabaseFaceIndexDataSource_Factory(faceEmbeddingDaoProvider);
  }

  public static LocalDatabaseFaceIndexDataSource newInstance(FaceEmbeddingDao faceEmbeddingDao) {
    return new LocalDatabaseFaceIndexDataSource(faceEmbeddingDao);
  }
}
