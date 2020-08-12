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
    public static MutableLiveData<Boolean> isNull = new MutableLiveData<>();


    private BlizzardRepository() {
    }

    public static MutableLiveData<WeatherData> getWeather(String cityName) {
        MutableLiveData<WeatherData> searchCityMutableLiveData = new MutableLiveData<>();
        new OpenWeatherService().getWeatherByCityName(cityName).enqueue(new Callback<WeatherData>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getName().isEmpty()) {
                        isNull.setValue(true);
                    } else {
                        isNull.setValue(false);
                    }
                    searchCityMutableLiveData.setValue(response.body());

                } else {
                    //response failed for some reason
                    isNull.setValue(true);
                    Log.e(TAG, "onResponse: Request Failed " + response.errorBody());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherData> call, Throwable t) {
                isNull.setValue(true);
                searchCityMutableLiveData.setValue(null);
            }
        });

        return searchCityMutableLiveData;
    }

    public static MutableLiveData<WeatherData> getWeather(Double lat, Double lon) {
        MutableLiveData<WeatherData> currentCityMutableLiveData = new MutableLiveData<>();
        new OpenWeatherService().getWeatherByLongitudeLatitude(lat, lon).enqueue(new Callback<WeatherData>() {
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
