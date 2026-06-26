package com.example.data.repository;

import com.example.data.local.dao.QuestionDao;
import com.example.data.local.preferences.UserPreferences;
import com.example.data.remote.QuizApi;
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
public final class QuizRepositoryImpl_Factory implements Factory<QuizRepositoryImpl> {
  private final Provider<QuestionDao> questionDaoProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  private final Provider<QuizApi> quizApiProvider;

  public QuizRepositoryImpl_Factory(Provider<QuestionDao> questionDaoProvider,
      Provider<UserPreferences> userPreferencesProvider, Provider<QuizApi> quizApiProvider) {
    this.questionDaoProvider = questionDaoProvider;
    this.userPreferencesProvider = userPreferencesProvider;
    this.quizApiProvider = quizApiProvider;
  }

  @Override
  public QuizRepositoryImpl get() {
    return newInstance(questionDaoProvider.get(), userPreferencesProvider.get(), quizApiProvider.get());
  }

  public static QuizRepositoryImpl_Factory create(Provider<QuestionDao> questionDaoProvider,
      Provider<UserPreferences> userPreferencesProvider, Provider<QuizApi> quizApiProvider) {
    return new QuizRepositoryImpl_Factory(questionDaoProvider, userPreferencesProvider, quizApiProvider);
  }

  public static QuizRepositoryImpl newInstance(QuestionDao questionDao,
      UserPreferences userPreferences, QuizApi quizApi) {
    return new QuizRepositoryImpl(questionDao, userPreferences, quizApi);
  }
}
