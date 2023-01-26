package com.shohiebsense.conversionapp.manager.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shohiebsense.conversionapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BaseNetworkModule {

    @Provides
    @Singleton
    fun provideGson() : Gson =
        GsonBuilder()
            .serializeNulls()
            .setLenient()
            .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        headerInterceptor: HeaderInterceptor
    ) : OkHttpClient =
        OkHttpClient.Builder().apply {
            interceptors().add(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            interceptors().add(headerInterceptor)
        }.build()

    @Provides
    @Singleton
    fun provideBaseUrl() : String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideHeaderInterceptor() = HeaderInterceptor()

    @Provides
    @Singleton
    fun provideRetrofit(
      baseUrl: String,
      gson: Gson,
      okHttpClient: OkHttpClient
    ) = Retrofit.Builder().apply {
        baseUrl(baseUrl)
        addConverterFactory(GsonConverterFactory.create(gson))
        client(okHttpClient)
    }.build()

}