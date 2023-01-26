package com.shohiebsense.conversionapp.manager.api

import com.shohiebsense.conversionapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Authorization", "Token ${BuildConfig.APP_ID}")
                    .build()
            )
        }
    }

}