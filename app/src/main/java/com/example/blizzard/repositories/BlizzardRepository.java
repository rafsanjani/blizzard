package com.example.blizzard.repositories;

import android.util.Log;

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
    private static final String TAG = "BlizzardRepository";
    private OpenWeatherService mOpenWeatherService;


    public BlizzardRepository() {
        mOpenWeatherService = new OpenWeatherService();
    }

    public MutableLiveData<WeatherData> getWeather(String cityName) {
        MutableLiveData<WeatherData> searchCityMutableLiveData = new MutableLiveData<>();
        mOpenWeatherService.getWeatherByCityName(cityName).enqueue(new Callback<WeatherData>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    searchCityMutableLiveData.setValue(response.body());
                } else {
                    //response failed for some reason
                    Log.e(TAG, "onResponse: Request Failed " + response.errorBody());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherData> call, Throwable t) {
                searchCityMutableLiveData.setValue(null);
            }
        });

        return searchCityMutableLiveData;
    }

    public MutableLiveData<WeatherData> getWeather(Double lat, Double lon) {
        MutableLiveData<WeatherData> currentCityMutableLiveData = new MutableLiveData<>();
        mOpenWeatherService.getWeatherByLongitudeLatitude(lat, lon).enqueue(new Callback<WeatherData>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    currentCityMutableLiveData.postValue(response.body());
                } else {
                    //response failed for some reason
                    Log.e(TAG, "onResponse: Request Failed " + response.errorBody());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherData> call, Throwable t) {
                currentCityMutableLiveData.setValue(null);
            }
        });

        return currentCityMutableLiveData;
    }
}
