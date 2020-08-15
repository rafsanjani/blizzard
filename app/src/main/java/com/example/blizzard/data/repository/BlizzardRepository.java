package com.example.blizzard.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.blizzard.data.database.WeatherDatabase;
import com.example.blizzard.data.entities.WeatherDataEntity;
import com.example.blizzard.model.OpenWeatherService;
import com.example.blizzard.model.WeatherDataResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

/**
 * Created by tony on 8/9/2020
 */

public class BlizzardRepository {
    private WeatherDatabase mWeatherDatabase;

    public BlizzardRepository(Context context) {
        mWeatherDatabase = WeatherDatabase.getInstance(context);
    }

    public List<WeatherDataEntity> getAllDataFromDb() {
        return mWeatherDatabase.weatherDao().getAllWeather();
    }

    public void saveWeatherData(WeatherDataEntity weatherDataEntity) {
        mWeatherDatabase.weatherDao().saveWeather(weatherDataEntity);
    }

    private static final String TAG = "BlizzardRepository";

    public MutableLiveData<WeatherDataResponse> getWeather(String cityName) {
        MutableLiveData<WeatherDataResponse> searchCityMutableLiveData = new MutableLiveData<>();
        new OpenWeatherService().getWeather(cityName).enqueue(new Callback<WeatherDataResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherDataResponse> call, Response<WeatherDataResponse> response) {
                if (response.isSuccessful()) {
                    searchCityMutableLiveData.setValue(response.body());

                } else {
                    //response failed for some reason
                    Log.e(TAG, "onResponse: Request Failed " + response.errorBody());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherDataResponse> call, Throwable t) {
                searchCityMutableLiveData.setValue(null);
            }
        });

        return searchCityMutableLiveData;
    }

    public MutableLiveData<WeatherDataResponse> getWeather(Double lat, Double lon) {
        MutableLiveData<WeatherDataResponse> currentCityMutableLiveData = new MutableLiveData<>();
        new OpenWeatherService().getWeather(lat, lon).enqueue(new Callback<WeatherDataResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherDataResponse> call, Response<WeatherDataResponse> response) {
                if (response.isSuccessful()) {
                    currentCityMutableLiveData.postValue(response.body());
                } else {
                    //response failed for some reason
                    Log.e(TAG, "onResponse: Request Failed " + response.errorBody());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherDataResponse> call, Throwable t) {
                currentCityMutableLiveData.setValue(null);
            }
        });

        return currentCityMutableLiveData;
    }


}
