package com.example.blizzard.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blizzard.data.entities.WeatherDataEntity;
import com.example.blizzard.data.repository.BlizzardRepository;
import com.example.blizzard.model.WeatherDataResponse;

import java.util.List;

/**
 * Created by tony on 8/9/2020
 */

public class BlizzardViewModel extends AndroidViewModel {

    private final BlizzardRepository mBlizzardRepository;
    private LiveData<WeatherDataResponse> mWeatherLiveData = new MutableLiveData<>();


    public BlizzardViewModel(@NonNull Application application) {
        super(application);
        mBlizzardRepository = new BlizzardRepository(application);
    }

    public void saveWeather(WeatherDataEntity weatherDataEntity) {
        mBlizzardRepository.saveWeatherData(weatherDataEntity);
    }

    public void getWeather(String cityName) {
        mWeatherLiveData = mBlizzardRepository.getWeather(cityName);
    }

    public WeatherDataEntity getWeatherByCityName(String cityName){
        return mBlizzardRepository.getWeatherByCityName(cityName);
    }

    public void updateWeatherData(WeatherDataEntity entity){
        mBlizzardRepository.updateWeather(entity);
    }

    public List<WeatherDataEntity> getAllDataFromDb() {
        return mBlizzardRepository.getAllDataFromDb();
    }

    public void getWeather(Double lat, Double lon) {
        mWeatherLiveData = mBlizzardRepository.getWeather(lat, lon);
    }

    public LiveData<WeatherDataResponse> getWeatherLiveData() {
        return mWeatherLiveData;
    }

    public LiveData<WeatherDataResponse> getWeatherByCity(String city) {
        return mBlizzardRepository.getWeather(city);
    }

}
