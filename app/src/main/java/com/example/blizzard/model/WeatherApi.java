package com.example.blizzard.model;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by kelvi on 8/3/2020
 */
public interface WeatherApi {


    @GET("weather")
    Single<WeatherData> getWeatherData(@Query("q") String city_name, @Query("appid") String api_key);
}
