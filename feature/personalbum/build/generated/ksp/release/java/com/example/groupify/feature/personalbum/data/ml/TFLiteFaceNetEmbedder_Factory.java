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
public final class TFLiteFaceNetEmbedder_Factory implements Factory<TFLiteFaceNetEmbedder> {
  private final Provider<Context> contextProvider;

  public TFLiteFaceNetEmbedder_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TFLiteFaceNetEmbedder get() {
    return newInstance(contextProvider.get());
  }

  public static TFLiteFaceNetEmbedder_Factory create(Provider<Context> contextProvider) {
    return new TFLiteFaceNetEmbedder_Factory(contextProvider);
  }

  public static TFLiteFaceNetEmbedder newInstance(Context context) {
    return new TFLiteFaceNetEmbedder(context);
  }
}
