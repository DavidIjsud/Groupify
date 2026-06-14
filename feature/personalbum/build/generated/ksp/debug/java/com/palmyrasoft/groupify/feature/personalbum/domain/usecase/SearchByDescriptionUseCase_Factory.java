package com.palmyrasoft.groupify.feature.personalbum.domain.usecase;

import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.TextQueryEmbedder;
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.DescriptionIndexRepository;
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
public final class SearchByDescriptionUseCase_Factory implements Factory<SearchByDescriptionUseCase> {
  private final Provider<TextQueryEmbedder> textQueryEmbedderProvider;

  private final Provider<DescriptionIndexRepository> descriptionIndexRepositoryProvider;

  private SearchByDescriptionUseCase_Factory(Provider<TextQueryEmbedder> textQueryEmbedderProvider,
      Provider<DescriptionIndexRepository> descriptionIndexRepositoryProvider) {
    this.textQueryEmbedderProvider = textQueryEmbedderProvider;
    this.descriptionIndexRepositoryProvider = descriptionIndexRepositoryProvider;
  }

  @Override
  public SearchByDescriptionUseCase get() {
    return newInstance(textQueryEmbedderProvider.get(), descriptionIndexRepositoryProvider.get());
  }

  public static SearchByDescriptionUseCase_Factory create(
      Provider<TextQueryEmbedder> textQueryEmbedderProvider,
      Provider<DescriptionIndexRepository> descriptionIndexRepositoryProvider) {
    return new SearchByDescriptionUseCase_Factory(textQueryEmbedderProvider, descriptionIndexRepositoryProvider);
  }

  public static SearchByDescriptionUseCase newInstance(TextQueryEmbedder textQueryEmbedder,
      DescriptionIndexRepository descriptionIndexRepository) {
    return new SearchByDescriptionUseCase(textQueryEmbedder, descriptionIndexRepository);
  }
}
