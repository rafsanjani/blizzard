package com.example.blizzard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.blizzard.model.WeatherData;
import com.example.blizzard.repositories.BlizzardRepository;


/**
 * Created by tony on 8/9/2020
 */

public class BlizzardViewModel extends ViewModel {

    private LiveData<WeatherData> mWeatherLiveData = new MutableLiveData<>();


    public void getWeather(String cityName) {
        BlizzardRepository.isNull.setValue(false);
        mWeatherLiveData = BlizzardRepository.getWeather(cityName);
    }

    public void getWeather(Double lat, Double lon) {
        mWeatherLiveData = BlizzardRepository.getWeather(lat, lon);
    }

    public LiveData<WeatherData> getWeatherLiveData() {
        return mWeatherLiveData;
    }

}
