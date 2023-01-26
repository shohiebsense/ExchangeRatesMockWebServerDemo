package com.shohiebsense.conversionapp.manager.model

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Message
import com.google.gson.Gson
import java.util.SortedMap

private const val KEY_LATEST_RESPONSE_CACHE = "latest_response_cache"

data class LatestResponse(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: SortedMap<String, Double>,
) : BaseApiResponse(
) {

    companion object {
        fun initFromCache(sharedPreferences: SharedPreferences): LatestResponse? {
            val cacheStr =
                sharedPreferences.getString(KEY_LATEST_RESPONSE_CACHE, null) ?: return null
            return Gson().fromJson(cacheStr, LatestResponse::class.java)
        }
    }

    fun toCache(editor: Editor) {
        editor.putString(KEY_LATEST_RESPONSE_CACHE, Gson().toJson(this)).apply()
    }

    private fun getOriginValue(currencyValue: Double): Double {
        return 1 / currencyValue
    }

    fun convert(convertRequest: ConvertRequest): List<Pair<String, Double>> {
        val convertedRates = sortedMapOf<String, Double>()
        val fromUsdCurrencyValue = rates[convertRequest.from.value]!!
        rates.forEach {
            val toUsdCurrencyValue = it.value!!
            val fromOriginValue = getOriginValue(fromUsdCurrencyValue)
            val convertedValue = toUsdCurrencyValue / fromOriginValue
            convertedRates[it.key] = convertedValue
        }
        return convertedRates.toList()
    }

}