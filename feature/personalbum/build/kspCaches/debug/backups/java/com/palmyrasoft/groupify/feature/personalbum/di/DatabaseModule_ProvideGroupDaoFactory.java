package com.palmyrasoft.groupify.feature.personalbum.di;

import com.palmyrasoft.groupify.feature.personalbum.data.local.PersonAlbumDatabase;
import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.GroupDao;
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
public final class DatabaseModule_ProvideGroupDaoFactory implements Factory<GroupDao> {
  private final Provider<PersonAlbumDatabase> dbProvider;

  private DatabaseModule_ProvideGroupDaoFactory(Provider<PersonAlbumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public GroupDao get() {
    return provideGroupDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideGroupDaoFactory create(
      Provider<PersonAlbumDatabase> dbProvider) {
    return new DatabaseModule_ProvideGroupDaoFactory(dbProvider);
  }

  public static GroupDao provideGroupDao(PersonAlbumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGroupDao(db));
  }
}
