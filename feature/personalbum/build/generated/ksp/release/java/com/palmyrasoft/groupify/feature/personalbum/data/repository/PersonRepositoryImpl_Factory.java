package com.palmyrasoft.groupify.feature.personalbum.data.repository;

import com.palmyrasoft.groupify.feature.personalbum.data.local.dao.PersonDao;
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
public final class PersonRepositoryImpl_Factory implements Factory<PersonRepositoryImpl> {
  private final Provider<PersonDao> personDaoProvider;

  private PersonRepositoryImpl_Factory(Provider<PersonDao> personDaoProvider) {
    this.personDaoProvider = personDaoProvider;
  }

  @Override
  public PersonRepositoryImpl get() {
    return newInstance(personDaoProvider.get());
  }

  public static PersonRepositoryImpl_Factory create(Provider<PersonDao> personDaoProvider) {
    return new PersonRepositoryImpl_Factory(personDaoProvider);
  }

  public static PersonRepositoryImpl newInstance(PersonDao personDao) {
    return new PersonRepositoryImpl(personDao);
  }
}
