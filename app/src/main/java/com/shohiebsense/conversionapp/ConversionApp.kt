package com.shohiebsense.conversionapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp



@HiltAndroidApp
class ConversionApp : Application() {
    companion object {
        const val APP_SHARED_PREFERENCES = "app_shared_preferences"
    }
}