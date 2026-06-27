package com.example.data.di;

import com.example.data.local.dao.QuestionDao;
import com.example.data.local.database.AppDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideQuestionDaoFactory implements Factory<QuestionDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideQuestionDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public QuestionDao get() {
    return provideQuestionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideQuestionDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideQuestionDaoFactory(databaseProvider);
  }

  public static QuestionDao provideQuestionDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideQuestionDao(database));
  }
}
