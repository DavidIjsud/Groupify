package com.palmyrasoft.groupify.feature.personalbum.data.ml;

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
public final class MlKitTextRecognizer_Factory implements Factory<MlKitTextRecognizer> {
  private final Provider<Context> contextProvider;

  private MlKitTextRecognizer_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MlKitTextRecognizer get() {
    return newInstance(contextProvider.get());
  }

  public static MlKitTextRecognizer_Factory create(Provider<Context> contextProvider) {
    return new MlKitTextRecognizer_Factory(contextProvider);
  }

  public static MlKitTextRecognizer newInstance(Context context) {
    return new MlKitTextRecognizer(context);
  }
}
