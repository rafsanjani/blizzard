package com.example.blizzard.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert
    void insertWeatherData(WeatherData weatherData);

    @Query("SELECT * FROM WeatherData")
    List<WeatherData> getAll();

    @Query("SELECT name FROM WeatherData WHERE name = :cityName")
    String getByName(String cityName);

    @Update
    void updateData(WeatherData weatherData);
}
