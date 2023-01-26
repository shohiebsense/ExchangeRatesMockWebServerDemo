package com.shohiebsense.conversionapp.manager.api.latestapi

import android.content.SharedPreferences
import android.util.Log
import com.shohiebsense.conversionapp.BuildConfig
import com.shohiebsense.conversionapp.manager.api.requestApi
import com.shohiebsense.conversionapp.manager.model.LatestResponse
import javax.inject.Inject
import kotlin.math.absoluteValue

private const val LAST_REQUEST_TIME_MILLISECONDS = "last_request_time_milliseconds"


private fun Long.getCurrentTimeInMinute() = (this / (1000 * 60)) % 60
private fun Long.getCurrentTimeInHour() = (this / (1000 * 60 * 60)) % 24

private fun isRapidRequest(lastRequestTimeInMilliSeconds: Long): Boolean {
    val currentTimeInMilliSeconds = System.currentTimeMillis()

    val currentHour = currentTimeInMilliSeconds.getCurrentTimeInHour()
    val lastrequestTimeInHour = lastRequestTimeInMilliSeconds.getCurrentTimeInHour()

    if (currentHour != lastrequestTimeInHour) {
        return false
    }

    return (currentTimeInMilliSeconds.getCurrentTimeInMinute() - lastRequestTimeInMilliSeconds.getCurrentTimeInMinute())
        .absoluteValue < BuildConfig.ACCESS_INTERVAL_LIMIT
}

class LatestRepository @Inject constructor(private val latestApi: LatestApi) {

    suspend fun requestLatest(sharedPreferences: SharedPreferences, onFailed: () -> Unit): LatestResponse {
        val cache = LatestResponse.initFromCache(sharedPreferences)
        val lastRequestTimeMilliSeconds =
            sharedPreferences.getLong(LAST_REQUEST_TIME_MILLISECONDS, 0L)
        val editor = sharedPreferences.edit()

        if (cache != null && isRapidRequest(lastRequestTimeMilliSeconds)) {
            return cache
        }

        val response = requestApi(result = LatestResponse::class.java) {
            val latestResponse = latestApi.getLatest()
            if(latestResponse.isSuccessful) {
                editor.putLong(LAST_REQUEST_TIME_MILLISECONDS, System.currentTimeMillis()).apply()
            } else {
                onFailed()
            }
            latestResponse
        }
        response.toCache(editor)
        return response
    }

}