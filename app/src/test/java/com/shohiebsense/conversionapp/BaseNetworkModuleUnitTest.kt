package com.shohiebsense.conversionapp

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shohiebsense.conversionapp.manager.api.BaseNetworkModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit

@RunWith(MockitoJUnitRunner::class)
class BaseNetworkModuleUnitTest {

    val baseNetworkModule = BaseNetworkModule

    @Test
    fun `provideGson should not throw an exception`() {
        baseNetworkModule.provideGson()
    }

    @Test
    fun `provideOkHttpClient should not throw an exception`(){
        baseNetworkModule.provideOkHttpClient(
            mock(Context::class.java),
            baseNetworkModule.provideHeaderInterceptor()
        )
    }

    @Test
    fun `provideBaseUrl should not throw an exception`(){
        baseNetworkModule.provideBaseUrl()
    }

    @Test
    fun `provideHeaderInterceptor should not throw an exception` (){
        baseNetworkModule.provideHeaderInterceptor()
    }

    @Test
    fun `provideRetrofit should not throw an exception`(){
        baseNetworkModule.provideRetrofit(
            baseNetworkModule.provideBaseUrl(),
            baseNetworkModule.provideGson(),
            baseNetworkModule.provideOkHttpClient(
                mock(Context::class.java),
                baseNetworkModule.provideHeaderInterceptor()
            )
        )
    }

    @Test
    fun `retrofit should have the base url`(){
        val retrofit = baseNetworkModule.provideRetrofit(
            baseNetworkModule.provideBaseUrl(),
            baseNetworkModule.provideGson(),
            baseNetworkModule.provideOkHttpClient(
                mock(Context::class.java),
                baseNetworkModule.provideHeaderInterceptor()
            )
        )

        assert(retrofit.baseUrl().toUrl().toString() == BuildConfig.BASE_URL)
    }


}