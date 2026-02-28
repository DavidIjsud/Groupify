package com.example.groupify.feature.personalbum.di;

import com.example.groupify.feature.personalbum.data.local.PersonAlbumDatabase;
import com.example.groupify.feature.personalbum.data.local.dao.PersonDao;
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
public final class DatabaseModule_ProvidePersonDaoFactory implements Factory<PersonDao> {
  private final Provider<PersonAlbumDatabase> dbProvider;

  public DatabaseModule_ProvidePersonDaoFactory(Provider<PersonAlbumDatabase> dbProvider) {
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
