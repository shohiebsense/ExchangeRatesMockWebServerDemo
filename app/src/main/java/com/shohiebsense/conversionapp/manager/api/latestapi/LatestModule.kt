package com.shohiebsense.conversionapp.manager.api.latestapi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LatestModule {

    @Provides
    @Singleton
    fun provideLatestRepository(latestApi: LatestApi) = LatestRepository(latestApi)

    @Provides
    @Singleton
    fun provideLatestApi(retrofit: Retrofit): LatestApi = retrofit.create(LatestApi::class.java)
}