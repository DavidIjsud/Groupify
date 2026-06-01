package com.palmyrasoft.groupify.feature.personalbum.data.source;

import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoTextDao;
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
public final class LocalDatabasePhotoTextDataSource_Factory implements Factory<LocalDatabasePhotoTextDataSource> {
  private final Provider<PhotoTextDao> daoProvider;

  private LocalDatabasePhotoTextDataSource_Factory(Provider<PhotoTextDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public LocalDatabasePhotoTextDataSource get() {
    return newInstance(daoProvider.get());
  }

  public static LocalDatabasePhotoTextDataSource_Factory create(
      Provider<PhotoTextDao> daoProvider) {
    return new LocalDatabasePhotoTextDataSource_Factory(daoProvider);
  }

  public static LocalDatabasePhotoTextDataSource newInstance(PhotoTextDao dao) {
    return new LocalDatabasePhotoTextDataSource(dao);
  }
}
