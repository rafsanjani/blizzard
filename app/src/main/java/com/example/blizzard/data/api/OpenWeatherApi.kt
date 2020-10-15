package com.example.blizzard.data.api

import com.example.blizzard.model.WeatherDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by kelvi on 8/3/2020
 */
interface OpenWeatherApi {
    @GET("weather?")
    suspend fun getWeatherByCityName(
            @Query("q") cityName: String?,
            @Query("appid") apiKey: String?
    ): WeatherDataResponse

    @GET("weather?")
    suspend fun getWeatherByLongitudeLatitude(
            @Query("lat") latitude: Double?,
            @Query("lon") longitude: Double?,
            @Query("appid") apiKey: String?
    ): WeatherDataResponse
}