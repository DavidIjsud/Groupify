package com.palmyrasoft.groupify.feature.personalbum.domain.usecase;

import com.palmyrasoft.groupify.feature.personalbum.domain.repository.GroupRepository;
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
public final class CreateGroupUseCase_Factory implements Factory<CreateGroupUseCase> {
  private final Provider<GroupRepository> groupRepositoryProvider;

  private CreateGroupUseCase_Factory(Provider<GroupRepository> groupRepositoryProvider) {
    this.groupRepositoryProvider = groupRepositoryProvider;
  }

  @Override
  public CreateGroupUseCase get() {
    return newInstance(groupRepositoryProvider.get());
  }

  public static CreateGroupUseCase_Factory create(
      Provider<GroupRepository> groupRepositoryProvider) {
    return new CreateGroupUseCase_Factory(groupRepositoryProvider);
  }

  public static CreateGroupUseCase newInstance(GroupRepository groupRepository) {
    return new CreateGroupUseCase(groupRepository);
  }
}
