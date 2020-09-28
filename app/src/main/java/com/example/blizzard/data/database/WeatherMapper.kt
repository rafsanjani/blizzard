package com.example.blizzard.data.database

import android.util.Log
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.model.WeatherDataResponse
import com.example.blizzard.viewmodel.BlizzardViewModel

/* Created by Rafsanjani on 15/08/2020. */
class WeatherMapper(private var viewModel: BlizzardViewModel) {
    suspend fun mapToEntity(weatherDataResponse: WeatherDataResponse): WeatherDataEntity {
        return checkIfAlreadyExists(weatherDataResponse)
    }

    private suspend fun checkIfAlreadyExists(weatherDataResponse: WeatherDataResponse): WeatherDataEntity {
        var exists = false
        try {
            val entity = viewModel.getWeatherByCityName(weatherDataResponse.name)
            exists = entity?.favourite!!
        } catch (e: NullPointerException) {
            Log.e(TAG, "checkIfAlreadyExists: Data not saved yet")
        }
        return WeatherDataEntity(
                weatherDataResponse.name!!,
                weatherDataResponse.sys?.country!!,
                weatherDataResponse.main?.temp!!,
                weatherDataResponse.main.humidity,
                weatherDataResponse.weather?.get(0)?.description!!,
                weatherDataResponse.wind?.speed!!,
                weatherDataResponse.dt,
                weatherDataResponse.timezone,
                exists
        )
    }

    companion object {
        private const val TAG = "WeatherMapper"
    }
}