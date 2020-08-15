package com.example.blizzard.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.blizzard.data.entities.WeatherDataEntity;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveWeather(WeatherDataEntity weatherDataEntity);

    @Query("SELECT * FROM weather")
    List<WeatherDataEntity> getAllWeather();


    @Query("SELECT cityName FROM weather WHERE cityName = :cityName")
    LiveData<String> getWeatherForCity(String cityName);

    @Update
    void updateWeatherData(WeatherDataEntity weatherDataEntity);
}
