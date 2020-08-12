package com.example.blizzard.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert
    void insertWeatherData(WeatherDataEntity weatherDataEntity);

    @Query("SELECT * FROM WeatherDataEntity")
    List<WeatherDataEntity> getAll();

    @Query("SELECT cityName FROM WeatherDataEntity WHERE cityName = :cityName")
    String getByName(String cityName);

    @Update
    void updateData(WeatherDataEntity weatherDataEntity);
}
