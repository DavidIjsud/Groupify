package com.palmyrasoft.groupify.feature.personalbum.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class IndexFacesWorker_AssistedFactory_Impl implements IndexFacesWorker_AssistedFactory {
  private final IndexFacesWorker_Factory delegateFactory;

  IndexFacesWorker_AssistedFactory_Impl(IndexFacesWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public IndexFacesWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<IndexFacesWorker_AssistedFactory> create(
      IndexFacesWorker_Factory delegateFactory) {
    return InstanceFactory.create(new IndexFacesWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<IndexFacesWorker_AssistedFactory> createFactoryProvider(
      IndexFacesWorker_Factory delegateFactory) {
    return InstanceFactory.create(new IndexFacesWorker_AssistedFactory_Impl(delegateFactory));
  }
}
