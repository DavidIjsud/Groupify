package com.example.groupify.feature.personalbum.data.thumbnail;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "deprecation"
})
public final class AndroidQueryFaceThumbnailGenerator_Factory implements Factory<AndroidQueryFaceThumbnailGenerator> {
  private final Provider<Context> contextProvider;

  public AndroidQueryFaceThumbnailGenerator_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AndroidQueryFaceThumbnailGenerator get() {
    return newInstance(contextProvider.get());
  }

  public static AndroidQueryFaceThumbnailGenerator_Factory create(
      Provider<Context> contextProvider) {
    return new AndroidQueryFaceThumbnailGenerator_Factory(contextProvider);
  }

  public static AndroidQueryFaceThumbnailGenerator newInstance(Context context) {
    return new AndroidQueryFaceThumbnailGenerator(context);
  }
}
