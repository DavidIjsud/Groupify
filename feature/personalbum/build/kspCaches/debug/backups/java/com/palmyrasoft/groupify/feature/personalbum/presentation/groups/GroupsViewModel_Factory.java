package com.palmyrasoft.groupify.feature.personalbum.presentation.groups;

import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.DeleteGroupUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.GetGroupUseCase;
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.GetGroupsUseCase;
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
public final class GroupsViewModel_Factory implements Factory<GroupsViewModel> {
  private final Provider<GetGroupsUseCase> getGroupsUseCaseProvider;

  private final Provider<GetGroupUseCase> getGroupUseCaseProvider;

  private final Provider<DeleteGroupUseCase> deleteGroupUseCaseProvider;

  private GroupsViewModel_Factory(Provider<GetGroupsUseCase> getGroupsUseCaseProvider,
      Provider<GetGroupUseCase> getGroupUseCaseProvider,
      Provider<DeleteGroupUseCase> deleteGroupUseCaseProvider) {
    this.getGroupsUseCaseProvider = getGroupsUseCaseProvider;
    this.getGroupUseCaseProvider = getGroupUseCaseProvider;
    this.deleteGroupUseCaseProvider = deleteGroupUseCaseProvider;
  }

  @Override
  public GroupsViewModel get() {
    return newInstance(getGroupsUseCaseProvider.get(), getGroupUseCaseProvider.get(), deleteGroupUseCaseProvider.get());
  }

  public static GroupsViewModel_Factory create(Provider<GetGroupsUseCase> getGroupsUseCaseProvider,
      Provider<GetGroupUseCase> getGroupUseCaseProvider,
      Provider<DeleteGroupUseCase> deleteGroupUseCaseProvider) {
    return new GroupsViewModel_Factory(getGroupsUseCaseProvider, getGroupUseCaseProvider, deleteGroupUseCaseProvider);
  }

  public static GroupsViewModel newInstance(GetGroupsUseCase getGroupsUseCase,
      GetGroupUseCase getGroupUseCase, DeleteGroupUseCase deleteGroupUseCase) {
    return new GroupsViewModel(getGroupsUseCase, getGroupUseCase, deleteGroupUseCase);
  }
}
