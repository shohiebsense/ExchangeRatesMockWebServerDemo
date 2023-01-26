package com.shohiebsense.conversionapp.ui.page.home

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shohiebsense.conversionapp.manager.api.latestapi.LatestRepository
import com.shohiebsense.conversionapp.manager.model.ConvertRequest
import com.shohiebsense.conversionapp.manager.model.LatestResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val latestRepository: LatestRepository
) : ViewModel() {
    var isInitialized = false
    var latestResponse: LatestResponse? = null
    val convertRequest = ConvertRequest()
    val currencyList = arrayListOf<String>()
    val currencyAndValueList = mutableStateListOf<Pair<String, Double>>()

    fun init(sharedPreferences: SharedPreferences, onFailed: () -> Unit) {
        viewModelScope.launch {
            latestResponse = latestRepository.requestLatest(sharedPreferences, onFailed)
            currencyList.clear()
            currencyList.addAll(latestResponse!!.rates.keys.toList())
            currencyAndValueList.clear()
            currencyAndValueList.addAll(latestResponse!!.rates.toList())
        }
    }

    fun convert() {
        if (!latestResponse?.rates?.containsKey(convertRequest.from.value)!!) {
            return
        }
        currencyAndValueList.clear()
        currencyAndValueList.addAll(latestResponse!!.convert(convertRequest))
    }


}