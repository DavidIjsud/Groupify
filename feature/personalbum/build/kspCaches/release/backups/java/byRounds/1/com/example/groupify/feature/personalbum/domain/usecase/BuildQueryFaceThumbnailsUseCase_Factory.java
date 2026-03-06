package com.example.groupify.feature.personalbum.domain.usecase;

import com.example.groupify.feature.personalbum.domain.thumbnail.QueryFaceThumbnailGenerator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class BuildQueryFaceThumbnailsUseCase_Factory implements Factory<BuildQueryFaceThumbnailsUseCase> {
  private final Provider<QueryFaceThumbnailGenerator> generatorProvider;

  public BuildQueryFaceThumbnailsUseCase_Factory(
      Provider<QueryFaceThumbnailGenerator> generatorProvider) {
    this.generatorProvider = generatorProvider;
  }

  @Override
  public BuildQueryFaceThumbnailsUseCase get() {
    return newInstance(generatorProvider.get());
  }

  public static BuildQueryFaceThumbnailsUseCase_Factory create(
      Provider<QueryFaceThumbnailGenerator> generatorProvider) {
    return new BuildQueryFaceThumbnailsUseCase_Factory(generatorProvider);
  }

  public static BuildQueryFaceThumbnailsUseCase newInstance(QueryFaceThumbnailGenerator generator) {
    return new BuildQueryFaceThumbnailsUseCase(generator);
  }
}
