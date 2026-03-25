package com.palmyrasoft.groupify.feature.personalbum.data.source;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AndroidMediaStorePhotoDataSource_Factory implements Factory<AndroidMediaStorePhotoDataSource> {
  private final Provider<Context> contextProvider;

  private AndroidMediaStorePhotoDataSource_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AndroidMediaStorePhotoDataSource get() {
    return newInstance(contextProvider.get());
  }

  public static AndroidMediaStorePhotoDataSource_Factory create(Provider<Context> contextProvider) {
    return new AndroidMediaStorePhotoDataSource_Factory(contextProvider);
  }

  public static AndroidMediaStorePhotoDataSource newInstance(Context context) {
    return new AndroidMediaStorePhotoDataSource(context);
  }
}
