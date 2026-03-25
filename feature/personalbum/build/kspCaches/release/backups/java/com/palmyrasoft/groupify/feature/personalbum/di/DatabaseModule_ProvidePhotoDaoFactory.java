package com.palmyrasoft.groupify.feature.personalbum.di;

import com.palmyrasoft.groupify.feature.personalbum.data.local.PersonAlbumDatabase;
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoDao;
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
public final class DatabaseModule_ProvidePhotoDaoFactory implements Factory<PhotoDao> {
  private final Provider<PersonAlbumDatabase> dbProvider;

  private DatabaseModule_ProvidePhotoDaoFactory(Provider<PersonAlbumDatabase> dbProvider) {
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
