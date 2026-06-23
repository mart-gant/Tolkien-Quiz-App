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
public final class GetQuestionsUseCase_Factory implements Factory<GetQuestionsUseCase> {
  private final Provider<QuizRepository> repositoryProvider;

  public GetQuestionsUseCase_Factory(Provider<QuizRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetQuestionsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetQuestionsUseCase_Factory create(Provider<QuizRepository> repositoryProvider) {
    return new GetQuestionsUseCase_Factory(repositoryProvider);
  }

  public static GetQuestionsUseCase newInstance(QuizRepository repository) {
    return new GetQuestionsUseCase(repository);
  }
}
