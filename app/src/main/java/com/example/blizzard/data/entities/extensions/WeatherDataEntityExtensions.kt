package com.example.blizzard.data.entities.extensions

import com.example.blizzard.data.entities.WeatherDataEntity
import kotlin.math.roundToInt

/**
Create by kelvin clark on 9/27/2020
 */

val WeatherDataEntity.IntCelsius: Int
    get() {
        return (this.temperature!! - 273.15).roundToInt()
    }