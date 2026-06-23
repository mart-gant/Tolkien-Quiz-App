package com.example.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
    "KotlinInternalInJava"
})
public final class QuizRepositoryImpl_Factory implements Factory<QuizRepositoryImpl> {
  @Override
  public QuizRepositoryImpl get() {
    return newInstance();
  }

  public static QuizRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static QuizRepositoryImpl newInstance() {
    return new QuizRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final QuizRepositoryImpl_Factory INSTANCE = new QuizRepositoryImpl_Factory();
  }
}
