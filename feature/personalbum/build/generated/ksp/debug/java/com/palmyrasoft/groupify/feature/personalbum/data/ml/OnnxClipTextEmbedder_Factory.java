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
public final class OnnxClipTextEmbedder_Factory implements Factory<OnnxClipTextEmbedder> {
  private final Provider<Context> contextProvider;

  private final Provider<ClipBpeTokenizer> tokenizerProvider;

  private OnnxClipTextEmbedder_Factory(Provider<Context> contextProvider,
      Provider<ClipBpeTokenizer> tokenizerProvider) {
    this.contextProvider = contextProvider;
    this.tokenizerProvider = tokenizerProvider;
  }

  @Override
  public OnnxClipTextEmbedder get() {
    return newInstance(contextProvider.get(), tokenizerProvider.get());
  }

  public static OnnxClipTextEmbedder_Factory create(Provider<Context> contextProvider,
      Provider<ClipBpeTokenizer> tokenizerProvider) {
    return new OnnxClipTextEmbedder_Factory(contextProvider, tokenizerProvider);
  }

  public static OnnxClipTextEmbedder newInstance(Context context, ClipBpeTokenizer tokenizer) {
    return new OnnxClipTextEmbedder(context, tokenizer);
  }
}
