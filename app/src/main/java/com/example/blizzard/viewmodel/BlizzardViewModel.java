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
    private BlizzardRepository mBlizzardRepository;
    private LiveData<WeatherData> mWeatherLiveData = new MutableLiveData<>();

    public void init() {
        mBlizzardRepository = new BlizzardRepository();
    }

    public void getWeather(String cityName) {
        mWeatherLiveData = mBlizzardRepository.getWeather(cityName);
    }

    public void getWeather(Double lat, Double lon) {
        mWeatherLiveData = mBlizzardRepository.getWeather(lat, lon);
    }

    public LiveData<WeatherData> getWeatherLiveData() {
        return mWeatherLiveData;
    }
}
