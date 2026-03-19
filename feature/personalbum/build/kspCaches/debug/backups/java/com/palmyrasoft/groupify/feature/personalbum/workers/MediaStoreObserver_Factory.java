package com.palmyrasoft.groupify.feature.personalbum.workers;

import androidx.work.WorkManager;
import com.palmyrasoft.groupify.feature.personalbum.data.prefs.IndexingOnboardingPrefs;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class MediaStoreObserver_Factory implements Factory<MediaStoreObserver> {
  private final Provider<WorkManager> workManagerProvider;

  private final Provider<IndexingOnboardingPrefs> indexingPrefsProvider;

  private MediaStoreObserver_Factory(Provider<WorkManager> workManagerProvider,
      Provider<IndexingOnboardingPrefs> indexingPrefsProvider) {
    this.workManagerProvider = workManagerProvider;
    this.indexingPrefsProvider = indexingPrefsProvider;
  }

  @Override
  public MediaStoreObserver get() {
    return newInstance(workManagerProvider.get(), indexingPrefsProvider.get());
  }

  public static MediaStoreObserver_Factory create(Provider<WorkManager> workManagerProvider,
      Provider<IndexingOnboardingPrefs> indexingPrefsProvider) {
    return new MediaStoreObserver_Factory(workManagerProvider, indexingPrefsProvider);
  }

  public static MediaStoreObserver newInstance(WorkManager workManager,
      IndexingOnboardingPrefs indexingPrefs) {
    return new MediaStoreObserver(workManager, indexingPrefs);
  }
}
