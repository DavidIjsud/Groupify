package com.example.groupify.feature.personalbum.data.repository;

import com.example.groupify.feature.personalbum.data.local.dao.PersonDao;
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
public final class PersonRepositoryImpl_Factory implements Factory<PersonRepositoryImpl> {
  private final Provider<PersonDao> personDaoProvider;

  public PersonRepositoryImpl_Factory(Provider<PersonDao> personDaoProvider) {
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
