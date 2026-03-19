package com.palmyrasoft.groupify.feature.personalbum.data.repository;

import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PhotoDao;
import com.palmyrasoft.groupify.feature.personalbum.data.source.AndroidMediaStorePhotoDataSource;
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
public final class PhotoRepositoryImpl_Factory implements Factory<PhotoRepositoryImpl> {
  private final Provider<AndroidMediaStorePhotoDataSource> dataSourceProvider;

  private final Provider<PhotoDao> photoDaoProvider;

  private PhotoRepositoryImpl_Factory(Provider<AndroidMediaStorePhotoDataSource> dataSourceProvider,
      Provider<PhotoDao> photoDaoProvider) {
    this.dataSourceProvider = dataSourceProvider;
    this.photoDaoProvider = photoDaoProvider;
  }

  @Override
  public PhotoRepositoryImpl get() {
    return newInstance(dataSourceProvider.get(), photoDaoProvider.get());
  }

  public static PhotoRepositoryImpl_Factory create(
      Provider<AndroidMediaStorePhotoDataSource> dataSourceProvider,
      Provider<PhotoDao> photoDaoProvider) {
    return new PhotoRepositoryImpl_Factory(dataSourceProvider, photoDaoProvider);
  }

  public static PhotoRepositoryImpl newInstance(AndroidMediaStorePhotoDataSource dataSource,
      PhotoDao photoDao) {
    return new PhotoRepositoryImpl(dataSource, photoDao);
  }
}
