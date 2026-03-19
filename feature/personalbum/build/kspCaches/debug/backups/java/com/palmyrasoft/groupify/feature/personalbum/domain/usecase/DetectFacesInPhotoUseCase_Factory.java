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
public final class DetectFacesInPhotoUseCase_Factory implements Factory<DetectFacesInPhotoUseCase> {
  private final Provider<FaceDetector> faceDetectorProvider;

  private DetectFacesInPhotoUseCase_Factory(Provider<FaceDetector> faceDetectorProvider) {
    this.faceDetectorProvider = faceDetectorProvider;
  }

  @Override
  public DetectFacesInPhotoUseCase get() {
    return newInstance(faceDetectorProvider.get());
  }

  public static DetectFacesInPhotoUseCase_Factory create(
      Provider<FaceDetector> faceDetectorProvider) {
    return new DetectFacesInPhotoUseCase_Factory(faceDetectorProvider);
  }

  public static DetectFacesInPhotoUseCase newInstance(FaceDetector faceDetector) {
    return new DetectFacesInPhotoUseCase(faceDetector);
  }
}
