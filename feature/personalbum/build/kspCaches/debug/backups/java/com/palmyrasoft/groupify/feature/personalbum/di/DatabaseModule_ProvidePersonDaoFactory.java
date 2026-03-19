package com.palmyrasoft.groupify.feature.personalbum.di;

import com.palmyrasoft.groupify.feature.personalbum.data.local.PersonAlbumDatabase;
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PersonDao;
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
public final class DatabaseModule_ProvidePersonDaoFactory implements Factory<PersonDao> {
  private final Provider<PersonAlbumDatabase> dbProvider;

  private DatabaseModule_ProvidePersonDaoFactory(Provider<PersonAlbumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PersonDao get() {
    return providePersonDao(dbProvider.get());
  }

  public static DatabaseModule_ProvidePersonDaoFactory create(
      Provider<PersonAlbumDatabase> dbProvider) {
    return new DatabaseModule_ProvidePersonDaoFactory(dbProvider);
  }

  public static PersonDao providePersonDao(PersonAlbumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePersonDao(db));
  }
}
