package com.palmyrasoft.groupify.feature.personalbum.domain.usecase;

import com.palmyrasoft.groupify.feature.personalbum.domain.detection.FaceDetector;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class DetectQueryFacesUseCase_Factory implements Factory<DetectQueryFacesUseCase> {
  private final Provider<FaceDetector> faceDetectorProvider;

  private DetectQueryFacesUseCase_Factory(Provider<FaceDetector> faceDetectorProvider) {
    this.faceDetectorProvider = faceDetectorProvider;
  }

  @Override
  public DetectQueryFacesUseCase get() {
    return newInstance(faceDetectorProvider.get());
  }

  public static DetectQueryFacesUseCase_Factory create(
      Provider<FaceDetector> faceDetectorProvider) {
    return new DetectQueryFacesUseCase_Factory(faceDetectorProvider);
  }

  public static DetectQueryFacesUseCase newInstance(FaceDetector faceDetector) {
    return new DetectQueryFacesUseCase(faceDetector);
  }
}
