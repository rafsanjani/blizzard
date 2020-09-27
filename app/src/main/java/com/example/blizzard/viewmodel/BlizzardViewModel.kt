package com.example.blizzard.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.data.repository.BlizzardRepository
import com.example.blizzard.model.WeatherDataResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Created by tony on 8/9/2020
 */
class BlizzardViewModel(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val mBlizzardRepository: BlizzardRepository = BlizzardRepository(application)
    private var response : WeatherDataResponse? = null

    fun saveAppState(cityName: String?) {
        savedStateHandle.set(CITY_NAME, cityName)
    }

    fun saveAppState(cityName: String?, searchBoxText: String?) {
        savedStateHandle.set(CITY_NAME, cityName)
        savedStateHandle.set(SEARCH_BOX_TEXT, searchBoxText)
    }

    val appState: Array<String?>
        get() {
            val appState = arrayOfNulls<String>(2)
            appState[0] = savedStateHandle.get<String>(CITY_NAME)
            appState[1] = savedStateHandle.get<String>(SEARCH_BOX_TEXT)
            return appState
        }

    fun saveWeather(weatherDataEntity: WeatherDataEntity?) {
        viewModelScope.launch(IO) { mBlizzardRepository.saveWeatherData(weatherDataEntity) }
    }

    suspend fun getWeatherByCityName(cityName: String?): WeatherDataEntity? {
        val job = viewModelScope.async(IO) { mBlizzardRepository.getWeatherByCityName(cityName) }
        job.await().let { weatherDataEntity ->
            return weatherDataEntity
        }
    }

    fun updateWeatherData(entity: WeatherDataEntity?) {
        viewModelScope.launch(IO) { mBlizzardRepository.updateWeather(entity) }
    }

    suspend fun getAll(): List<WeatherDataEntity?>? {
        val job = viewModelScope.async(IO) { mBlizzardRepository.getAll() }
        job.await().let { weatherDataEntityList -> return weatherDataEntityList }
    }

    fun getWeather(lat: Double?, lon: Double?) = liveData {
        try {
            response = mBlizzardRepository.getWeather(lat, lon)
            emit(response)
            Log.i(TAG, "getWeather: weather data acquired")
        } catch (e: Throwable) {
            Log.e(TAG, "getWeather: Error getting data", e)
        }
    }

    fun getWeather(cityName: String) = liveData {
        try {
            response = mBlizzardRepository.getWeather(cityName)
            emit(response)
            Log.i(TAG, "getWeather: weather data acquired")
        } catch (e: Throwable) {
            Log.e(TAG, "getWeather: Error getting weather data", e)
        }
    }

    companion object {
        const val CITY_NAME = "com.example.blizzard.viewmodel.cityName"
        const val SEARCH_BOX_TEXT = "com.example.blizzard.viewmodel.searchBoxText"
        const val TAG = "BlizzardViewModel"
    }
}