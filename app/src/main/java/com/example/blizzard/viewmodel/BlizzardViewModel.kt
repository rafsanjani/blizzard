package com.example.blizzard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.data.repository.BlizzardRepository
import com.example.blizzard.model.WeatherDataResponse

/**
 * Created by tony on 8/9/2020
 */
class BlizzardViewModel(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val mBlizzardRepository: BlizzardRepository = BlizzardRepository(application)
    var weatherLiveData: LiveData<WeatherDataResponse?> = MutableLiveData()
        private set

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
        mBlizzardRepository.saveWeatherData(weatherDataEntity)
    }

    fun getWeather(cityName: String?) {
        weatherLiveData = mBlizzardRepository.getWeather(cityName)
    }

    fun getWeatherByCityName(cityName: String?): WeatherDataEntity? {
        return mBlizzardRepository.getWeatherByCityName(cityName)
    }

    fun updateWeatherData(entity: WeatherDataEntity?) {
        mBlizzardRepository.updateWeather(entity)
    }

    val allDataFromDb: List<WeatherDataEntity?>?
        get() = mBlizzardRepository.allDataFromDb

    fun getWeather(lat: Double?, lon: Double?) {
        weatherLiveData = mBlizzardRepository.getWeather(lat, lon)
    }

    companion object {
        const val CITY_NAME = "com.example.blizzard.viewmodel.cityName"
        const val SEARCH_BOX_TEXT = "com.example.blizzard.viewmodel.searchBoxText"
    }

}