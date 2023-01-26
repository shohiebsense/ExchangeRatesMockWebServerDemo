package com.shohiebsense.conversionapp

import com.google.gson.Gson
import com.shohiebsense.conversionapp.manager.api.latestapi.LatestApi
import com.shohiebsense.conversionapp.manager.api.latestapi.LatestModule
import com.shohiebsense.conversionapp.manager.api.requestApi
import com.shohiebsense.conversionapp.manager.model.LatestResponse
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class LatestEndpoint {

    fun remoteApi(baseUrl: String): LatestApi {
        return Retrofit.Builder()
            .client(OkHttpClient())
            .baseUrl(baseUrl)
            .build()
            .create(LatestApi::class.java)
    }

    private val mockWebServer = MockWebServer()
    val mockResponse = MockResponse()
        .setResponseCode(200)
        .setBody(sampleResponseJsonStr)

    val latestModule = LatestModule
    private lateinit var retrofit: Retrofit
    lateinit var api: LatestApi


    @Before
    fun setup() {
        mockWebServer.start()
        mockWebServer.url(BuildConfig.BASE_URL)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `check successful response`() = runTest {

        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody(sampleResponseJsonStr) // sample JSON
        mockResponse.throttleBody(1024, 1, TimeUnit.SECONDS)
        mockWebServer.enqueue(mockedResponse)

        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LatestApi::class.java)


        val latest = apiService.getLatest()
        assertEquals(latest.body(), Gson().fromJson(sampleResponseJsonStr,LatestResponse::class.java) )
    }

    @Test
    fun testSuccessfulResponse() {


        latestModule.provideLatestApi(
            provideRetrofit()
        )

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