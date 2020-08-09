package com.example.blizzard.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blizzard.model.WeatherData;
import com.example.blizzard.repositories.BlizzardRepository;

/**
 * Created by tony on 8/9/2020
 */

public class BlizzardViewModel extends ViewModel {
    private BlizzardRepository mBlizzardRepository;
    private MutableLiveData<WeatherData> mSearchedCityWeatherDataLiveData;
    private MutableLiveData<WeatherData> mCurrentCityWeatherDataLiveData;

    public void init() {
        mBlizzardRepository = new BlizzardRepository();
        mSearchedCityWeatherDataLiveData = new MutableLiveData<>();
        mCurrentCityWeatherDataLiveData = new MutableLiveData<>();
    }

    public void getWeatherByCityName(String cityName) {
        mSearchedCityWeatherDataLiveData = mBlizzardRepository.getWeatherByCityName(cityName);
    }

    public void getWeatherByLongitudeLatitude(Double lat, Double lon) {
        mCurrentCityWeatherDataLiveData = mBlizzardRepository.getWeatherByLongitudeLatitude(lat, lon);
    }

    public MutableLiveData<WeatherData> getSearchedCityWeatherDataLiveData() {
        return mSearchedCityWeatherDataLiveData;
    }

    public MutableLiveData<WeatherData> getCurrentCityWeatherDataLiveData() {
        return mCurrentCityWeatherDataLiveData;
    }
}
