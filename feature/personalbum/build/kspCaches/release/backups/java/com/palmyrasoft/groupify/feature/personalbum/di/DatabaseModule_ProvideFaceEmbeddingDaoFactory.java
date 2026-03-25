package com.palmyrasoft.groupify.feature.personalbum.di;

import com.palmyrasoft.groupify.feature.personalbum.data.local.PersonAlbumDatabase;
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.FaceEmbeddingDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideFaceEmbeddingDaoFactory implements Factory<FaceEmbeddingDao> {
  private final Provider<PersonAlbumDatabase> dbProvider;

  private DatabaseModule_ProvideFaceEmbeddingDaoFactory(Provider<PersonAlbumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FaceEmbeddingDao get() {
    return provideFaceEmbeddingDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideFaceEmbeddingDaoFactory create(
      Provider<PersonAlbumDatabase> dbProvider) {
    return new DatabaseModule_ProvideFaceEmbeddingDaoFactory(dbProvider);
  }

  public static FaceEmbeddingDao provideFaceEmbeddingDao(PersonAlbumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideFaceEmbeddingDao(db));
  }
}
