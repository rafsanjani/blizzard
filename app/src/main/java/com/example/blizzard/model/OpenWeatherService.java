package com.example.blizzard.model;

import com.example.blizzard.Util.ApiKeyHolder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

/**
 * Created by kelvi on 8/3/2020
 */
public  class OpenWeatherService {

    private final static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private OpenWeatherApi mOpenWeatherApi;

    public OpenWeatherService() {
        mOpenWeatherApi = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApi.class);
    }

    public Call<WeatherData> getWeather(String cityName){

        return mOpenWeatherApi.getWeather(cityName, ApiKeyHolder.getApi_key());
    }
}
