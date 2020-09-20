package com.example.blizzard.data.database;

/* Created by Rafsanjani on 15/08/2020. */

import android.util.Log;

import com.example.blizzard.data.entities.WeatherDataEntity;
import com.example.blizzard.data.repository.BlizzardRepository;
import com.example.blizzard.model.WeatherDataResponse;
import com.example.blizzard.viewmodel.BlizzardViewModel;

public class WeatherMapper {
    BlizzardViewModel viewModel;
    private static final String TAG = "WeatherMapper";

    public WeatherMapper(BlizzardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public WeatherDataEntity mapToEntity(WeatherDataResponse weatherDataResponse) {

        return checkIfAlreadyExists(weatherDataResponse);
    }

    private WeatherDataEntity checkIfAlreadyExists(WeatherDataResponse weatherDataResponse) {
        Boolean exists = false;
        try {
            WeatherDataEntity entity = viewModel.getWeatherByCityName(weatherDataResponse.getName());
            exists = entity.getFavourite();
        } catch (NullPointerException e) {
            Log.e(TAG, "checkIfAlreadyExists: Data not saved yet");
        }

        return new WeatherDataEntity(
                weatherDataResponse.getName(),
                weatherDataResponse.getSys().getCountry(),
                weatherDataResponse.getMain().getTemp(),
                weatherDataResponse.getMain().getHumidity(),
                weatherDataResponse.getWeather().get(0).getDescription(),
                weatherDataResponse.getWind().getSpeed(),
                weatherDataResponse.getDt(),
                weatherDataResponse.getTimezone(),
                exists
        );
    }

    public static WeatherDataResponse mapToResponse(WeatherDataEntity weatherDataEntity) {
        //todo: do actual conversion
        return new WeatherDataResponse();
    }
}
