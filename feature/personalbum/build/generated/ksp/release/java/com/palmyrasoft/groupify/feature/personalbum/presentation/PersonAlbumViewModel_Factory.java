package com.palmyrasoft.groupify.feature.personalbum.presentation;

import androidx.work.WorkManager;
import com.palmyrasoft.groupify.feature.personalbum.data.prefs.IndexingOnboardingPrefs;
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.DescriptionIndexRepository;
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.FaceIndexRepository;
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PhotoTextRepository;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.AddPhotosToGroupUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.BuildQueryFaceThumbnailsUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.CreateGroupUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.DetectQueryFacesUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.GetGroupsUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.SearchByDescriptionUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.SearchByPhotoUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.SearchByTextUseCase;
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

  private final Provider<SearchByTextUseCase> searchByTextUseCaseProvider;

  private final Provider<SearchByDescriptionUseCase> searchByDescriptionUseCaseProvider;

  private final Provider<DetectQueryFacesUseCase> detectQueryFacesUseCaseProvider;

  private final Provider<BuildQueryFaceThumbnailsUseCase> buildQueryFaceThumbnailsUseCaseProvider;

  private final Provider<FaceIndexRepository> faceIndexRepositoryProvider;

  private final Provider<PhotoTextRepository> photoTextRepositoryProvider;

  private final Provider<DescriptionIndexRepository> descriptionIndexRepositoryProvider;

  private final Provider<IndexingOnboardingPrefs> onboardingPrefsProvider;

  private final Provider<GetGroupsUseCase> getGroupsUseCaseProvider;

  private final Provider<CreateGroupUseCase> createGroupUseCaseProvider;

  private final Provider<AddPhotosToGroupUseCase> addPhotosToGroupUseCaseProvider;

  private PersonAlbumViewModel_Factory(Provider<WorkManager> workManagerProvider,
      Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider,
      Provider<SearchByTextUseCase> searchByTextUseCaseProvider,
      Provider<SearchByDescriptionUseCase> searchByDescriptionUseCaseProvider,
      Provider<DetectQueryFacesUseCase> detectQueryFacesUseCaseProvider,
      Provider<BuildQueryFaceThumbnailsUseCase> buildQueryFaceThumbnailsUseCaseProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<PhotoTextRepository> photoTextRepositoryProvider,
      Provider<DescriptionIndexRepository> descriptionIndexRepositoryProvider,
      Provider<IndexingOnboardingPrefs> onboardingPrefsProvider,
      Provider<GetGroupsUseCase> getGroupsUseCaseProvider,
      Provider<CreateGroupUseCase> createGroupUseCaseProvider,
      Provider<AddPhotosToGroupUseCase> addPhotosToGroupUseCaseProvider) {
    this.workManagerProvider = workManagerProvider;
    this.searchByPhotoUseCaseProvider = searchByPhotoUseCaseProvider;
    this.searchByTextUseCaseProvider = searchByTextUseCaseProvider;
    this.searchByDescriptionUseCaseProvider = searchByDescriptionUseCaseProvider;
    this.detectQueryFacesUseCaseProvider = detectQueryFacesUseCaseProvider;
    this.buildQueryFaceThumbnailsUseCaseProvider = buildQueryFaceThumbnailsUseCaseProvider;
    this.faceIndexRepositoryProvider = faceIndexRepositoryProvider;
    this.photoTextRepositoryProvider = photoTextRepositoryProvider;
    this.descriptionIndexRepositoryProvider = descriptionIndexRepositoryProvider;
    this.onboardingPrefsProvider = onboardingPrefsProvider;
    this.getGroupsUseCaseProvider = getGroupsUseCaseProvider;
    this.createGroupUseCaseProvider = createGroupUseCaseProvider;
    this.addPhotosToGroupUseCaseProvider = addPhotosToGroupUseCaseProvider;
  }

  @Override
  public PersonAlbumViewModel get() {
    return newInstance(workManagerProvider.get(), searchByPhotoUseCaseProvider.get(), searchByTextUseCaseProvider.get(), searchByDescriptionUseCaseProvider.get(), detectQueryFacesUseCaseProvider.get(), buildQueryFaceThumbnailsUseCaseProvider.get(), faceIndexRepositoryProvider.get(), photoTextRepositoryProvider.get(), descriptionIndexRepositoryProvider.get(), onboardingPrefsProvider.get(), getGroupsUseCaseProvider.get(), createGroupUseCaseProvider.get(), addPhotosToGroupUseCaseProvider.get());
  }

  public static PersonAlbumViewModel_Factory create(Provider<WorkManager> workManagerProvider,
      Provider<SearchByPhotoUseCase> searchByPhotoUseCaseProvider,
      Provider<SearchByTextUseCase> searchByTextUseCaseProvider,
      Provider<SearchByDescriptionUseCase> searchByDescriptionUseCaseProvider,
      Provider<DetectQueryFacesUseCase> detectQueryFacesUseCaseProvider,
      Provider<BuildQueryFaceThumbnailsUseCase> buildQueryFaceThumbnailsUseCaseProvider,
      Provider<FaceIndexRepository> faceIndexRepositoryProvider,
      Provider<PhotoTextRepository> photoTextRepositoryProvider,
      Provider<DescriptionIndexRepository> descriptionIndexRepositoryProvider,
      Provider<IndexingOnboardingPrefs> onboardingPrefsProvider,
      Provider<GetGroupsUseCase> getGroupsUseCaseProvider,
      Provider<CreateGroupUseCase> createGroupUseCaseProvider,
      Provider<AddPhotosToGroupUseCase> addPhotosToGroupUseCaseProvider) {
    return new PersonAlbumViewModel_Factory(workManagerProvider, searchByPhotoUseCaseProvider, searchByTextUseCaseProvider, searchByDescriptionUseCaseProvider, detectQueryFacesUseCaseProvider, buildQueryFaceThumbnailsUseCaseProvider, faceIndexRepositoryProvider, photoTextRepositoryProvider, descriptionIndexRepositoryProvider, onboardingPrefsProvider, getGroupsUseCaseProvider, createGroupUseCaseProvider, addPhotosToGroupUseCaseProvider);
  }

  public static PersonAlbumViewModel newInstance(WorkManager workManager,
      SearchByPhotoUseCase searchByPhotoUseCase, SearchByTextUseCase searchByTextUseCase,
      SearchByDescriptionUseCase searchByDescriptionUseCase,
      DetectQueryFacesUseCase detectQueryFacesUseCase,
      BuildQueryFaceThumbnailsUseCase buildQueryFaceThumbnailsUseCase,
      FaceIndexRepository faceIndexRepository, PhotoTextRepository photoTextRepository,
      DescriptionIndexRepository descriptionIndexRepository,
      IndexingOnboardingPrefs onboardingPrefs, GetGroupsUseCase getGroupsUseCase,
      CreateGroupUseCase createGroupUseCase, AddPhotosToGroupUseCase addPhotosToGroupUseCase) {
    return new PersonAlbumViewModel(workManager, searchByPhotoUseCase, searchByTextUseCase, searchByDescriptionUseCase, detectQueryFacesUseCase, buildQueryFaceThumbnailsUseCase, faceIndexRepository, photoTextRepository, descriptionIndexRepository, onboardingPrefs, getGroupsUseCase, createGroupUseCase, addPhotosToGroupUseCase);
  }
}
