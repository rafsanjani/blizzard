package com.example.blizzard.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.blizzard.data.entities.WeatherDataEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeather(weatherDataEntity: WeatherDataEntity?)

    @get:Query("SELECT * FROM weather")
    val allWeather: List<WeatherDataEntity?>?

    @Query("SELECT cityName FROM weather WHERE cityName = :cityName")
    fun getWeatherLiveDataForCity(cityName: String?): LiveData<String?>?

    @Query("SELECT * FROM weather WHERE cityName = :cityName")
    fun getWeatherForCity(cityName: String?): WeatherDataEntity?

    @Update
    fun updateWeatherData(weatherDataEntity: WeatherDataEntity?)
}