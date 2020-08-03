package com.example.blizzard.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kelvi on 8/3/2020
 */
public interface OpenWeatherApi {
    @GET("weather?")
    Call<WeatherData> getWeather(@Query("q") String cityName, @Query("appid") String apiKey);

}
