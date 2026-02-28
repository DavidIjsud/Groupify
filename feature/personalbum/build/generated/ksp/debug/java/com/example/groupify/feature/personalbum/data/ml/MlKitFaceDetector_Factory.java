package com.example.groupify.feature.personalbum.data.ml;

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
public final class MlKitFaceDetector_Factory implements Factory<MlKitFaceDetector> {
  private final Provider<Context> contextProvider;

  public MlKitFaceDetector_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MlKitFaceDetector get() {
    return newInstance(contextProvider.get());
  }

  public static MlKitFaceDetector_Factory create(Provider<Context> contextProvider) {
    return new MlKitFaceDetector_Factory(contextProvider);
  }

  public static MlKitFaceDetector newInstance(Context context) {
    return new MlKitFaceDetector(context);
  }
}
