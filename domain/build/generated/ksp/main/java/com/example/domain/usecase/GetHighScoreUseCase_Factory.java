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
public final class GetHighScoreUseCase_Factory implements Factory<GetHighScoreUseCase> {
  private final Provider<QuizRepository> repositoryProvider;

  public GetHighScoreUseCase_Factory(Provider<QuizRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetHighScoreUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetHighScoreUseCase_Factory create(Provider<QuizRepository> repositoryProvider) {
    return new GetHighScoreUseCase_Factory(repositoryProvider);
  }

  public static GetHighScoreUseCase newInstance(QuizRepository repository) {
    return new GetHighScoreUseCase(repository);
  }
}
