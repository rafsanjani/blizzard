package com.example.blizzard.model;

import com.example.blizzard.Utils.ApiKey;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;

/**
 * Created by kelvi on 8/3/2020
 */
public class WeatherApiService {
    private static final String BASE_URL = "api.openweathermap.org/data/2.5";

    private ApiKey mApiKey = new ApiKey();
    private WeatherApi api;

    public WeatherApiService() {
        api = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(WeatherApi.class);
    }

    public Single<List<WeatherData>> getWeatherData(String cityName){
        return api.getWeatherData(cityName, mApiKey.getApi_key());
    }
}
