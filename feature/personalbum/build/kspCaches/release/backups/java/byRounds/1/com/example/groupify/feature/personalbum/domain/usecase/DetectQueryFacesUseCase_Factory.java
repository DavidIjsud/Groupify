package com.example.groupify.feature.personalbum.domain.usecase;

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector;
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
public final class DetectQueryFacesUseCase_Factory implements Factory<DetectQueryFacesUseCase> {
  private final Provider<FaceDetector> faceDetectorProvider;

  public DetectQueryFacesUseCase_Factory(Provider<FaceDetector> faceDetectorProvider) {
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
