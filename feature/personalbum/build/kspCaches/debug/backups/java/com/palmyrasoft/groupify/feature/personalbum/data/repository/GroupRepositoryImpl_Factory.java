package com.palmyrasoft.groupify.feature.personalbum.data.repository;

import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.GroupDao;
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
public final class GroupRepositoryImpl_Factory implements Factory<GroupRepositoryImpl> {
  private final Provider<GroupDao> groupDaoProvider;

  private GroupRepositoryImpl_Factory(Provider<GroupDao> groupDaoProvider) {
    this.groupDaoProvider = groupDaoProvider;
  }

  @Override
  public GroupRepositoryImpl get() {
    return newInstance(groupDaoProvider.get());
  }

  public static GroupRepositoryImpl_Factory create(Provider<GroupDao> groupDaoProvider) {
    return new GroupRepositoryImpl_Factory(groupDaoProvider);
  }

  public static GroupRepositoryImpl newInstance(GroupDao groupDao) {
    return new GroupRepositoryImpl(groupDao);
  }
}
