package com.example.groupify.feature.personalbum.workers;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = IndexFacesWorker.class
)
public interface IndexFacesWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.example.groupify.feature.personalbum.workers.IndexFacesWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(IndexFacesWorker_AssistedFactory factory);
}
