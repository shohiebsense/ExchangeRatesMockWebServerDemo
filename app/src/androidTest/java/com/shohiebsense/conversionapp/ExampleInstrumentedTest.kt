package com.shohiebsense.conversionapp

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shohiebsense.conversionapp.manager.api.BaseNetworkModule
import com.shohiebsense.conversionapp.manager.api.latestapi.LatestApi
import com.shohiebsense.conversionapp.manager.api.latestapi.LatestModule
import com.shohiebsense.conversionapp.manager.api.latestapi.LatestRepository
import com.shohiebsense.conversionapp.ui.page.home.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Retrofit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    lateinit var latestApi: LatestApi
    lateinit var retrofit: Retrofit
    lateinit var latestRepository : LatestRepository
    lateinit var homeViewModel : HomeViewModel
    var activityScenario = ActivityScenario.launch(MainActivity::class.java)
    lateinit var context : Context


    @Before
    fun setup() {
        activityScenario.onActivity {
            context = it
            retrofit = BaseNetworkModule.provideRetrofit(
                BaseNetworkModule.provideBaseUrl(),
                BaseNetworkModule.provideGson(),
                BaseNetworkModule.provideOkHttpClient(
                    it,
                    BaseNetworkModule.provideHeaderInterceptor()
                )
            )
            latestApi = LatestModule.provideLatestApi(retrofit)
            latestRepository = LatestRepository(latestApi)
            homeViewModel = HomeViewModel(latestRepository)
        }
    }

    @Test
    fun isInitializedShouldBeFalseToBeginWith() {
        assert(!homeViewModel.isInitialized)
    }

    @Test
    fun latestResponseShouldBeNullToBeginWith() {
        assert(homeViewModel.latestResponse == null)
    }

    @Test
    fun currencyListAndCurrencyAndValueListShouldNotBeEmptyAfterInitIsCalled() = runTest {
        launch {
            homeViewModel.init(
                context.getSharedPreferences(
                    ConversionApp.APP_SHARED_PREFERENCES,
                    Context.MODE_PRIVATE
                )
            ) {
                //error toast message or anything
            }
        }
        launch {
            delay(2000L)
        }

        advanceUntilIdle()
        assert(homeViewModel.currencyList.isNotEmpty())
        assert(homeViewModel.currencyAndValueList.isNotEmpty())
    }

}