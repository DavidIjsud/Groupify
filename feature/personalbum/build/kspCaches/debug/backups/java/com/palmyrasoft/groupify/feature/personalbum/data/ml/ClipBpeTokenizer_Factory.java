package com.palmyrasoft.groupify.feature.personalbum.data.ml;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ClipBpeTokenizer_Factory implements Factory<ClipBpeTokenizer> {
  private final Provider<Context> contextProvider;

  private ClipBpeTokenizer_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ClipBpeTokenizer get() {
    return newInstance(contextProvider.get());
  }

  public static ClipBpeTokenizer_Factory create(Provider<Context> contextProvider) {
    return new ClipBpeTokenizer_Factory(contextProvider);
  }

  public static ClipBpeTokenizer newInstance(Context context) {
    return new ClipBpeTokenizer(context);
  }
}
