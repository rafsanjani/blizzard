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

    @Query("SELECT uuid FROM WeatherData WHERE uuid = :id")
    int getByUuid(int id);

    @Update
    void updateData(WeatherData weatherData);
}
