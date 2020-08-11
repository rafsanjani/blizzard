package com.example.blizzard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blizzard.Util.DatabaseInjector;
import com.example.blizzard.model.WeatherDao;
import com.example.blizzard.model.WeatherData;
import com.example.blizzard.repositories.BlizzardRepository;

import java.lang.ref.WeakReference;

/**
 * Created by tony on 8/9/2020
 */

public class BlizzardViewModel extends ViewModel {
    private BlizzardRepository mBlizzardRepository;
    private LiveData<WeatherData> mSearchedCityWeatherDataLiveData = new MutableLiveData<>();
    private LiveData<WeatherData> mCurrentCityWeatherDataLiveData = new MutableLiveData<>();


    public void init() {
        mBlizzardRepository = new BlizzardRepository();
    }

    public void getWeatherByCityName(String cityName) {
        mSearchedCityWeatherDataLiveData = mBlizzardRepository.getWeatherByCityName(cityName);
    }

    public void getWeatherByLongitudeLatitude(Double lat, Double lon) {
        mCurrentCityWeatherDataLiveData = mBlizzardRepository.getWeatherByLongitudeLatitude(lat, lon);
    }

    public LiveData<WeatherData> getSearchedCityWeatherDataLiveData() {
        return mSearchedCityWeatherDataLiveData;
    }

    public LiveData<WeatherData> getCurrentCityWeatherDataLiveData() {
        return mCurrentCityWeatherDataLiveData;
    }

}
