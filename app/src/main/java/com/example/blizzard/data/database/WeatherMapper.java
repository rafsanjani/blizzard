package com.example.blizzard.data.database;

/* Created by Rafsanjani on 15/08/2020. */

import com.example.blizzard.data.entities.WeatherDataEntity;
import com.example.blizzard.model.WeatherDataResponse;

public class WeatherMapper {
    public static WeatherDataEntity mapToEntity(WeatherDataResponse weatherDataResponse) {
        return new WeatherDataEntity(
                weatherDataResponse.getName(),
                weatherDataResponse.getMain().getTemp(),
                weatherDataResponse.getMain().getHumidity(),
                weatherDataResponse.getWeather().get(0).getDescription(),
                weatherDataResponse.getWind().getSpeed()
        );
    }

    public static WeatherDataResponse mapToResponse(WeatherDataEntity weatherDataEntity) {
        //todo: do actual conversion
        return new WeatherDataResponse();
    }
}
