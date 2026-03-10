package com.palmyrasoft.groupify.feature.personalbum.data.prefs;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "deprecation"
})
public final class IndexingOnboardingPrefs_Factory implements Factory<IndexingOnboardingPrefs> {
  private final Provider<Context> contextProvider;

  public IndexingOnboardingPrefs_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public IndexingOnboardingPrefs get() {
    return newInstance(contextProvider.get());
  }

  public static IndexingOnboardingPrefs_Factory create(Provider<Context> contextProvider) {
    return new IndexingOnboardingPrefs_Factory(contextProvider);
  }

  public static IndexingOnboardingPrefs newInstance(Context context) {
    return new IndexingOnboardingPrefs(context);
  }
}
