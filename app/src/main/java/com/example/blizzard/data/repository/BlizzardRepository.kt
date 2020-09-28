package com.example.blizzard.data.repository

import android.content.Context
import com.example.blizzard.data.api.OpenWeatherApi
import com.example.blizzard.data.database.WeatherDatabase
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.model.WeatherDataResponse
import com.example.blizzard.util.ApiKeyHolder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by tony on 8/9/2020
 */
class BlizzardRepository(context: Context) {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    private val openWeatherService: OpenWeatherApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)

    private val mWeatherDatabase: WeatherDatabase = WeatherDatabase.getInstance(context)

    fun getAll(): List<WeatherDataEntity?>? {
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

    suspend fun getWeather(cityName: String?): WeatherDataResponse {
        return openWeatherService.getWeatherByCityName(cityName, ApiKeyHolder.API_KEY)
    }

    suspend fun getWeather(lat: Double?, lon: Double?): WeatherDataResponse {
        return openWeatherService.getWeatherByLongitudeLatitude(lat, lon, ApiKeyHolder.API_KEY)
    }

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    }
}