package com.example.blizzard.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kelvi on 8/3/2020
 */
public interface OpenWeatherApi {
    @GET("weather?")
    Call<WeatherData> getWeatherByCityName(
            @Query("q") String cityName,
            @Query("appid") String apiKey
    );

    @GET("weather?")
    Call<WeatherData> getWeatherByLongitudeLatitude(
            @Query("lat") Double latitude,
            @Query("lon") Double longitude,
            @Query("appid") String apiKey
    );

}
