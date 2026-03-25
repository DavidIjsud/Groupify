package com.palmyrasoft.groupify.feature.personalbum.presentation;

import androidx.work.WorkManager;
import com.palmyrasoft.groupify.feature.personalbum.data.prefs.IndexingOnboardingPrefs;
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.FaceIndexRepository;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.BuildQueryFaceThumbnailsUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.DetectQueryFacesUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.SearchByPhotoUseCase;
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
public final class PersonAlbumViewModel_Factory implements Factory<PersonAlbumViewModel> {
  private final Provider<WorkManager> workManagerProvider;

  private final Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider;

  private final Provider<DetectQueryFacesUseCase> detectQueryFacesUseCaseProvider;

  private final Provider<BuildQueryFaceThumbnailsUseCase> buildQueryFaceThumbnailsUseCaseProvider;

  private final Provider<FaceIndexRepository> faceIndexRepositoryProvider;

  private final Provider<IndexingOnboardingPrefs> onboardingPrefsProvider;

  private PersonAlbumViewModel_Factory(Provider<WorkManager> workManagerProvider,
      Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider,
      Provider<DetectQueryFacesUseCase> detectQueryFacesUseCaseProvider,
      Provider<BuildQueryFaceThumbnailsUseCase> buildQueryFaceThumbnailsUseCaseProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<IndexingOnboardingPrefs> onboardingPrefsProvider) {
    this.workManagerProvider = workManagerProvider;
    this.searchByPhotoUseCaseProvider = searchByPhotoUseCaseProvider;
    this.detectQueryFacesUseCaseProvider = detectQueryFacesUseCaseProvider;
    this.buildQueryFaceThumbnailsUseCaseProvider = buildQueryFaceThumbnailsUseCaseProvider;
    this.faceIndexRepositoryProvider = faceIndexRepositoryProvider;
    this.onboardingPrefsProvider = onboardingPrefsProvider;
  }

  @Override
  public PersonAlbumViewModel get() {
    return newInstance(workManagerProvider.get(), searchByPhotoUseCaseProvider.get(), detectQueryFacesUseCaseProvider.get(), buildQueryFaceThumbnailsUseCaseProvider.get(), faceIndexRepositoryProvider.get(), onboardingPrefsProvider.get());
  }

  public static PersonAlbumViewModel_Factory create(Provider<WorkManager> workManagerProvider,
      Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider,
      Provider<DetectQueryFacesUseCase> detectQueryFacesUseCaseProvider,
      Provider<BuildQueryFaceThumbnailsUseCase> buildQueryFaceThumbnailsUseCaseProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<IndexingOnboardingPrefs> onboardingPrefsProvider) {
    return new PersonAlbumViewModel_Factory(workManagerProvider, searchByPhotoUseCaseProvider, detectQueryFacesUseCaseProvider, buildQueryFaceThumbnailsUseCaseProvider, faceIndexRepositoryProvider, onboardingPrefsProvider);
  }

  public static PersonAlbumViewModel newInstance(WorkManager workManager,
      SearchByPhotoUseCase searchByPhotoUseCase, DetectQueryFacesUseCase detectQueryFacesUseCase,
      BuildQueryFaceThumbnailsUseCase buildQueryFaceThumbnailsUseCase,
      FaceIndexRepository faceIndexRepository, IndexingOnboardingPrefs onboardingPrefs) {
    return new PersonAlbumViewModel(workManager, searchByPhotoUseCase, detectQueryFacesUseCase, buildQueryFaceThumbnailsUseCase, faceIndexRepository, onboardingPrefs);
  }
}
