package com.example.groupify.feature.personalbum.di;

import com.example.groupify.feature.personalbum.data.local.PersonAlbumDatabase;
import com.example.groupify.feature.personalbum.data.local.dao.FaceEmbeddingDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideFaceEmbeddingDaoFactory implements Factory<FaceEmbeddingDao> {
  private final Provider<PersonAlbumDatabase> dbProvider;

  public DatabaseModule_ProvideFaceEmbeddingDaoFactory(Provider<PersonAlbumDatabase> dbProvider) {
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
