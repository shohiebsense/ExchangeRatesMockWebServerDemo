package com.shohiebsense.conversionapp.manager.api.latestapi

import com.shohiebsense.conversionapp.manager.model.LatestResponse
import retrofit2.Response
import retrofit2.http.GET

interface LatestApi {

    @GET("api/latest.json")
    suspend fun getLatest() : Response<LatestResponse>

}