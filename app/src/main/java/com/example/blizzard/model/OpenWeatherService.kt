package com.example.blizzard.model

import com.example.blizzard.data.api.OpenWeatherApi
import com.example.blizzard.util.ApiKeyHolder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by kelvi on 8/3/2020
 */
class OpenWeatherService {
    private val mOpenWeatherApi: OpenWeatherApi
    fun getWeather(cityName: String?): Call<WeatherDataResponse?>? {
        return mOpenWeatherApi.getWeatherByCityName(cityName, ApiKeyHolder.API_KEY)
    }

    fun getWeather(latitude: Double?, longitude: Double?): Call<WeatherDataResponse?>? {
        return mOpenWeatherApi.getWeatherByLongitudeLatitude(latitude, longitude, ApiKeyHolder.API_KEY)
    }

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        mOpenWeatherApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApi::class.java)
    }
}