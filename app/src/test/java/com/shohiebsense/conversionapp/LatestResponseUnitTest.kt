package com.shohiebsense.conversionapp

import android.app.Activity
import com.google.gson.Gson
import com.shohiebsense.conversionapp.manager.model.ConvertRequest
import com.shohiebsense.conversionapp.manager.model.LatestResponse
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


class TestActivity : Activity()



@RunWith(JUnit4::class)
class LatestRepositoryUnitTest {

    private val latestResponse =  Gson().fromJson(sampleResponseJsonStr, LatestResponse::class.java)
    private val convertRequest = ConvertRequest()



    @Test
    fun `disclaimer should not be blank`() {
        assert(latestResponse.disclaimer.isNotBlank())
    }

    @Test
    fun `license should not be blank`() {
        assert(latestResponse.license.isNotBlank())
    }

    @Test
    fun `timeStamp should not be negative`() {
        assert(latestResponse.timestamp > 0L)
    }

    @Test
    fun `base should not be blank`() {
        assert(latestResponse.base.isNotBlank())
    }

    @Test
    fun `base should be USD`() {
        assert(latestResponse.base.equals("USD", true))
    }

    @Test
    fun `rates should not be empty`() {
        assert(latestResponse.rates.isNotEmpty())
    }

    @Test
    fun `convert should return convertedRates`() {
        assert(latestResponse.convert(convertRequest).isNotEmpty())
    }


    @Test
    fun `convert should return convertedRates size the same as the original rates`() {
        assert(latestResponse.convert(convertRequest).size == latestResponse.rates.size)
    }
}