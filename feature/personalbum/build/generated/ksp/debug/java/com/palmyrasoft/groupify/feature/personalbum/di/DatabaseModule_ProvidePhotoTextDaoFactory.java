package com.palmyrasoft.groupify.feature.personalbum.di;

import com.palmyrasoft.groupify.feature.personalbum.data.local.PersonAlbumDatabase;
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoTextDao;
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
public final class DatabaseModule_ProvidePhotoTextDaoFactory implements Factory<PhotoTextDao> {
  private final Provider<PersonAlbumDatabase> dbProvider;

  private DatabaseModule_ProvidePhotoTextDaoFactory(Provider<PersonAlbumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PhotoTextDao get() {
    return providePhotoTextDao(dbProvider.get());
  }

  public static DatabaseModule_ProvidePhotoTextDaoFactory create(
      Provider<PersonAlbumDatabase> dbProvider) {
    return new DatabaseModule_ProvidePhotoTextDaoFactory(dbProvider);
  }

  public static PhotoTextDao providePhotoTextDao(PersonAlbumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePhotoTextDao(db));
  }
}
