package com.example.blizzard.data.api;

import com.example.blizzard.model.WeatherDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kelvi on 8/3/2020
 */
public interface OpenWeatherApi {
    @GET("weather?")
    Call<WeatherDataResponse> getWeatherByCityName(
            @Query("q") String cityName,
            @Query("appid") String apiKey
    );

    @GET("weather?")
    Call<WeatherDataResponse> getWeatherByLongitudeLatitude(
            @Query("lat") Double latitude,
            @Query("lon") Double longitude,
            @Query("appid") String apiKey
    );

}
