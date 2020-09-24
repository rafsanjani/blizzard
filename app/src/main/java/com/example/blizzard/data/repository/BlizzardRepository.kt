package com.example.blizzard.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.blizzard.data.database.WeatherDatabase
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.model.OpenWeatherService
import com.example.blizzard.model.WeatherDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.internal.EverythingIsNonNull

/**
 * Created by tony on 8/9/2020
 */
class BlizzardRepository(context: Context?) {
    private val mWeatherDatabase: WeatherDatabase = WeatherDatabase.getInstance(context)

    fun getAll() : List<WeatherDataEntity?>? {
        return mWeatherDatabase.weatherDao()?.allWeather
    }


    fun saveWeatherData(weatherDataEntity: WeatherDataEntity?) {
        mWeatherDatabase.weatherDao()?.saveWeather(weatherDataEntity)
    }

    fun getWeatherByCityName(cityName: String?): WeatherDataEntity? {
        return mWeatherDatabase.weatherDao()?.getWeatherForCity(cityName)
    }

    fun updateWeather(entity: WeatherDataEntity?) {
        mWeatherDatabase.weatherDao()?.updateWeatherData(entity)
    }

    fun getWeather(cityName: String?): MutableLiveData<WeatherDataResponse?> {
        val searchCityMutableLiveData = MutableLiveData<WeatherDataResponse?>()
        OpenWeatherService().getWeather(cityName)?.enqueue(object : Callback<WeatherDataResponse?> {
            @EverythingIsNonNull
            override fun onResponse(call: Call<WeatherDataResponse?>, response: Response<WeatherDataResponse?>) {
                if (response.isSuccessful) {
                    searchCityMutableLiveData.setValue(response.body())
                } else {
                    //response failed for some reason
                    Log.e(TAG, "onResponse: Request Failed " + response.errorBody())
                    searchCityMutableLiveData.setValue(null)
                }
            }

            @EverythingIsNonNull
            override fun onFailure(call: Call<WeatherDataResponse?>, t: Throwable) {
                searchCityMutableLiveData.value = null
            }
        })
        return searchCityMutableLiveData
    }

    fun getWeather(lat: Double?, lon: Double?): MutableLiveData<WeatherDataResponse?> {
        val currentCityMutableLiveData = MutableLiveData<WeatherDataResponse?>()
        OpenWeatherService().getWeather(lat, lon)?.enqueue(object : Callback<WeatherDataResponse?> {
            @EverythingIsNonNull
            override fun onResponse(call: Call<WeatherDataResponse?>, response: Response<WeatherDataResponse?>) {
                if (response.isSuccessful) {
                    currentCityMutableLiveData.postValue(response.body())
                } else {
                    //response failed for some reason
                    Log.e(TAG, "onResponse: Request Failed " + response.errorBody())
                }
            }

            @EverythingIsNonNull
            override fun onFailure(call: Call<WeatherDataResponse?>, t: Throwable) {
                currentCityMutableLiveData.value = null
            }
        })
        return currentCityMutableLiveData
    }

    companion object {
        private const val TAG = "BlizzardRepository"
    }

}