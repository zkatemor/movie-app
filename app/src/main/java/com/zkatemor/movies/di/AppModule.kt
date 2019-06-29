package com.zkatemor.movies.di

import com.zkatemor.movies.BuildConfig
import com.zkatemor.movies.network.BASE_SEARCH_URL
import com.zkatemor.movies.network.BASE_URL
import com.zkatemor.movies.network.MoviesService
import com.zkatemor.movies.network.SearchService
import com.zkatemor.movies.presenter.MainPresenter
import com.zkatemor.movies.util.MoviesRepository
import com.zkatemor.movies.util.SearchRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun providePresenter(): MainPresenter = MainPresenter()

    @Singleton
    @Provides
    fun buildHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(interceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    @Named("movie_retrofit")
    fun buildRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named("search_retrofit")
    fun buildRetrofitSearch(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoviesApi(@Named("movie_retrofit") retrofit: Retrofit): MoviesService {
        return retrofit.create(MoviesService::class.java)
    }

    @Singleton
    @Provides
    @Named("Movies_Repository")
    fun provideMoviesRepository(moviesApi: MoviesService): MoviesRepository {
        return MoviesRepository(moviesApi)
    }

    @Singleton
    @Provides
    fun provideSearchApi(@Named("search_retrofit") retrofit: Retrofit): SearchService {
        return retrofit.create(SearchService::class.java)
    }

    @Singleton
    @Provides
    @Named("Search_Repository")
    fun provideSearchRepository(searchApi: SearchService): SearchRepository {
        return SearchRepository(searchApi)
    }
}