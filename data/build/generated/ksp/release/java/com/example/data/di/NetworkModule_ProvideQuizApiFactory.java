package com.example.data.di;

import com.example.data.remote.QuizApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideQuizApiFactory implements Factory<QuizApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_ProvideQuizApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public QuizApi get() {
    return provideQuizApi(okHttpClientProvider.get());
  }

  public static NetworkModule_ProvideQuizApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvideQuizApiFactory(okHttpClientProvider);
  }

  public static QuizApi provideQuizApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideQuizApi(okHttpClient));
  }
}
