package com.palmyrasoft.groupify.feature.personalbum.workers;

import androidx.work.WorkManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "deprecation"
})
public final class MediaStoreObserver_Factory implements Factory<MediaStoreObserver> {
  private final Provider<WorkManager> workManagerProvider;

  public MediaStoreObserver_Factory(Provider<WorkManager> workManagerProvider) {
    this.workManagerProvider = workManagerProvider;
  }

  @Override
  public MediaStoreObserver get() {
    return newInstance(workManagerProvider.get());
  }

  public static MediaStoreObserver_Factory create(Provider<WorkManager> workManagerProvider) {
    return new MediaStoreObserver_Factory(workManagerProvider);
  }

  public static MediaStoreObserver newInstance(WorkManager workManager) {
    return new MediaStoreObserver(workManager);
  }
}
