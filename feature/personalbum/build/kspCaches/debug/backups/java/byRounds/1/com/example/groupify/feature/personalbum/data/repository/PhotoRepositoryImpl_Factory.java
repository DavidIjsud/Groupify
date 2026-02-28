package com.example.groupify.feature.personalbum.data.repository;

import com.example.groupify.feature.personalbum.data.local.dao.PhotoDao;
import com.example.groupify.feature.personalbum.data.source.AndroidMediaStorePhotoDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class PhotoRepositoryImpl_Factory implements Factory<PhotoRepositoryImpl> {
  private final Provider<AndroidMediaStorePhotoDataSource> dataSourceProvider;

  private final Provider<PhotoDao> photoDaoProvider;

  public PhotoRepositoryImpl_Factory(Provider<AndroidMediaStorePhotoDataSource> dataSourceProvider,
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
