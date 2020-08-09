package com.example.blizzard.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.blizzard.model.OpenWeatherService;
import com.example.blizzard.model.WeatherData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

/**
 * Created by tony on 8/9/2020
 */

public class BlizzardRepository {
    private OpenWeatherService mOpenWeatherService;


    public BlizzardRepository() {
        mOpenWeatherService = new OpenWeatherService();
    }

    public MutableLiveData<WeatherData> getWeatherByCityName(String cityName) {
        MutableLiveData<WeatherData> SearchCityMutableLiveData = new MutableLiveData<>();
        mOpenWeatherService.getWeatherByCityName(cityName).enqueue(new Callback<WeatherData>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    SearchCityMutableLiveData.setValue(response.body());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherData> call, Throwable t) {
                SearchCityMutableLiveData.setValue(null);
            }
        });

        return SearchCityMutableLiveData;
    }

    public MutableLiveData<WeatherData> getWeatherByLongitudeLatitude(Double lat, Double lon) {
        MutableLiveData<WeatherData> CurrentCityMutableLiveData = new MutableLiveData<>();
        mOpenWeatherService.getWeatherByLongitudeLatitude(lat, lon).enqueue(new Callback<WeatherData>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    CurrentCityMutableLiveData.setValue(response.body());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherData> call, Throwable t) {
                CurrentCityMutableLiveData.setValue(null);
            }
        });

        return CurrentCityMutableLiveData;
    }
}
