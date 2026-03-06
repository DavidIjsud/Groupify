package com.example.groupify.feature.personalbum.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.example.groupify.feature.personalbum.domain.usecase.IndexFacesAndEmbeddingsUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "deprecation"
})
public final class IndexFacesWorker_Factory {
  private final Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider;

  private final Provider<IndexingNotificationHelper> notificationHelperProvider;

  public IndexFacesWorker_Factory(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<IndexingNotificationHelper> notificationHelperProvider) {
    this.indexFacesAndEmbeddingsUseCaseProvider = indexFacesAndEmbeddingsUseCaseProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public IndexFacesWorker get(Context appContext, WorkerParameters params) {
    return newInstance(appContext, params, indexFacesAndEmbeddingsUseCaseProvider.get(), notificationHelperProvider.get());
  }

  public static IndexFacesWorker_Factory create(
      Provider<IndexFacesAndEmbeddingsUseCase> indexFacesAndEmbeddingsUseCaseProvider,
      Provider<IndexingNotificationHelper> notificationHelperProvider) {
    return new IndexFacesWorker_Factory(indexFacesAndEmbeddingsUseCaseProvider, notificationHelperProvider);
  }

  public static IndexFacesWorker newInstance(Context appContext, WorkerParameters params,
      IndexFacesAndEmbeddingsUseCase indexFacesAndEmbeddingsUseCase,
      IndexingNotificationHelper notificationHelper) {
    return new IndexFacesWorker(appContext, params, indexFacesAndEmbeddingsUseCase, notificationHelper);
  }
}
