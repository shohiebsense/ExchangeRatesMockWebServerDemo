package com.shohiebsense.conversionapp.manager.api

import com.google.gson.Gson
import com.shohiebsense.conversionapp.manager.model.BaseApiResponse
import retrofit2.Response

suspend fun <T: BaseApiResponse> requestApi(
    result: Class<T>,
    execute: suspend () -> Response<T>,
) : T {
    val responseState : T
    val response = execute()

    if (!response.isSuccessful) {
        val errorResponse = response.raw()
        responseState  = Gson().fromJson(generateExceptionErrorMessage(errorResponse.message), result)
        return responseState
    }

    responseState = response.body()!!
    return responseState
}

fun generateExceptionErrorMessage(message: String?) : String {
    return "{\"isError\":\"true\",\"message\":\"$message\"}"
}