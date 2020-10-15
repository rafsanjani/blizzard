package com.example.blizzard.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherDataEntity(@field:PrimaryKey var cityName: String,
                        var country: String?, var temperature: Double?,
                        var humidity: Int?, var description: String?,
                        var windSpeed: Double?, var dt: Int?,
                        var timeZone: Int?, var favourite: Boolean?)