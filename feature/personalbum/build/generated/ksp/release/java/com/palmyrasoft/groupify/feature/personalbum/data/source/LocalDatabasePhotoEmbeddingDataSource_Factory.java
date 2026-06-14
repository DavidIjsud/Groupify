package com.palmyrasoft.groupify.feature.personalbum.data.source;

import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoEmbeddingDao;
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
public final class LocalDatabasePhotoEmbeddingDataSource_Factory implements Factory<LocalDatabasePhotoEmbeddingDataSource> {
  private final Provider<PhotoEmbeddingDao> daoProvider;

  private LocalDatabasePhotoEmbeddingDataSource_Factory(Provider<PhotoEmbeddingDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public LocalDatabasePhotoEmbeddingDataSource get() {
    return newInstance(daoProvider.get());
  }

  public static LocalDatabasePhotoEmbeddingDataSource_Factory create(
      Provider<PhotoEmbeddingDao> daoProvider) {
    return new LocalDatabasePhotoEmbeddingDataSource_Factory(daoProvider);
  }

  public static LocalDatabasePhotoEmbeddingDataSource newInstance(PhotoEmbeddingDao dao) {
    return new LocalDatabasePhotoEmbeddingDataSource(dao);
  }
}
