package com.example.groupify.feature.personalbum.di;

import com.example.groupify.feature.personalbum.data.local.PersonAlbumDatabase;
import com.example.groupify.feature.personalbum.data.local.dao.PhotoDao;
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
public final class DatabaseModule_ProvidePhotoDaoFactory implements Factory<PhotoDao> {
  private final Provider<PersonAlbumDatabase> dbProvider;

  public DatabaseModule_ProvidePhotoDaoFactory(Provider<PersonAlbumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PhotoDao get() {
    return providePhotoDao(dbProvider.get());
  }

  public static DatabaseModule_ProvidePhotoDaoFactory create(
      Provider<PersonAlbumDatabase> dbProvider) {
    return new DatabaseModule_ProvidePhotoDaoFactory(dbProvider);
  }

  public static PhotoDao providePhotoDao(PersonAlbumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePhotoDao(db));
  }
}
