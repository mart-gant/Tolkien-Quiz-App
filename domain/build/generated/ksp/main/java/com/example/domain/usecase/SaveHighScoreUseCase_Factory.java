package com.example.domain.usecase;

import com.example.domain.repository.QuizRepository;
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
    "KotlinInternalInJava"
})
public final class SaveHighScoreUseCase_Factory implements Factory<SaveHighScoreUseCase> {
  private final Provider<QuizRepository> repositoryProvider;

  public SaveHighScoreUseCase_Factory(Provider<QuizRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SaveHighScoreUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SaveHighScoreUseCase_Factory create(Provider<QuizRepository> repositoryProvider) {
    return new SaveHighScoreUseCase_Factory(repositoryProvider);
  }

  public static SaveHighScoreUseCase newInstance(QuizRepository repository) {
    return new SaveHighScoreUseCase(repository);
  }
}
