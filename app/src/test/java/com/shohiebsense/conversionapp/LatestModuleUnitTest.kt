package com.shohiebsense.conversionapp

import android.content.Context
import com.shohiebsense.conversionapp.manager.api.BaseNetworkModule
import com.shohiebsense.conversionapp.manager.api.latestapi.LatestModule
import com.shohiebsense.conversionapp.manager.api.requestApi
import com.shohiebsense.conversionapp.manager.model.LatestResponse
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit


val baseNetworkModule = BaseNetworkModule


fun provideRetrofit(): Retrofit = baseNetworkModule.provideRetrofit(
    baseNetworkModule.provideBaseUrl(),
    baseNetworkModule.provideGson(),
    baseNetworkModule.provideOkHttpClient(
        Mockito.mock(Context::class.java),
        baseNetworkModule.provideHeaderInterceptor()
    )
)

@RunWith(MockitoJUnitRunner::class)
class LatestModuleUnitTest {

    private val mockWebServer = MockWebServer()
    val latestModule = LatestModule

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testSuccessfulResponse() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(sampleResponseJsonStr)
            }
        }
    }

    @Test
    fun `provideLatestRepository should not throw an exception`() {
        latestModule.provideLatestRepository(
            latestModule.provideLatestApi(provideRetrofit())
        )
    }

    @Test
    fun `provideLatestApi should not throw an exception`() {
        latestModule.provideLatestApi(
            provideRetrofit()
        )
    }

    @Test
    fun `should successfully return the rates`() = runTest {
        val response = requestApi(result = LatestResponse::class.java) {
            val latestResponse = latestModule.provideLatestApi(provideRetrofit())
            latestResponse.getLatest()
        }
        assert(response.rates.isNotEmpty())
    }

}