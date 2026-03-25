package com.palmyrasoft.groupify.feature.personalbum.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.palmyrasoft.groupify.feature.personalbum.data.prefs.IndexingOnboardingPrefs;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.IndexFacesAndEmbeddingsUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class IndexFacesWorker_Factory {
  private final Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider;

  private final Provider<IndexingNotificationHelper> notificationHelperProvider;

  private final Provider<IndexingOnboardingPrefs> indexingPrefsProvider;

  private IndexFacesWorker_Factory(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<IndexingNotificationHelper> notificationHelperProvider,
      Provider<IndexingOnboardingPrefs> indexingPrefsProvider) {
    this.indexFacesAndEmbeddingsUseCaseProvider = indexFacesAndEmbeddingsUseCaseProvider;
    this.notificationHelperProvider = notificationHelperProvider;
    this.indexingPrefsProvider = indexingPrefsProvider;
  }

  public IndexFacesWorker get(Context appContext, WorkerParameters params) {
    return newInstance(appContext, params, indexFacesAndEmbeddingsUseCaseProvider.get(), notificationHelperProvider.get(), indexingPrefsProvider.get());
  }

  public static IndexFacesWorker_Factory create(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<IndexingNotificationHelper> notificationHelperProvider,
      Provider<IndexingOnboardingPrefs> indexingPrefsProvider) {
    return new IndexFacesWorker_Factory(indexFacesAndEmbeddingsUseCaseProvider, notificationHelperProvider, indexingPrefsProvider);
  }

  public static IndexFacesWorker newInstance(Context appContext, WorkerParameters params,
      IndexFacesAndEmbeddingsUseCase indexFacesAndEmbeddingsUseCase,
      IndexingNotificationHelper notificationHelper, IndexingOnboardingPrefs indexingPrefs) {
    return new IndexFacesWorker(appContext, params, indexFacesAndEmbeddingsUseCase, notificationHelper, indexingPrefs);
  }
}
