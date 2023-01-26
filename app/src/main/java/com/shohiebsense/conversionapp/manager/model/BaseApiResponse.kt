package com.shohiebsense.conversionapp.manager.model

abstract class BaseApiResponse(
    val isError: Boolean = false,
    val message: String = ""
)