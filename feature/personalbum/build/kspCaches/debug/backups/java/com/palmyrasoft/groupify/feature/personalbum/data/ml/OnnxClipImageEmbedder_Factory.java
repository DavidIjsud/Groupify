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
public final class OnnxClipImageEmbedder_Factory implements Factory<OnnxClipImageEmbedder> {
  private final Provider<Context> contextProvider;

  private OnnxClipImageEmbedder_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public OnnxClipImageEmbedder get() {
    return newInstance(contextProvider.get());
  }

  public static OnnxClipImageEmbedder_Factory create(Provider<Context> contextProvider) {
    return new OnnxClipImageEmbedder_Factory(contextProvider);
  }

  public static OnnxClipImageEmbedder newInstance(Context context) {
    return new OnnxClipImageEmbedder(context);
  }
}
