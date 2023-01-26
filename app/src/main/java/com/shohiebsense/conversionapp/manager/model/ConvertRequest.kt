package com.shohiebsense.conversionapp.manager.model

import androidx.compose.runtime.mutableStateOf

class ConvertRequest {
    val value = mutableStateOf(1)
    val from = mutableStateOf("USD")

}