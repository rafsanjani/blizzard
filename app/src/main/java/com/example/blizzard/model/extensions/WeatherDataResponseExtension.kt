package com.example.blizzard.model.extensions

import com.example.blizzard.model.WeatherDataResponse
import kotlin.math.roundToInt

/**
Create by kelvin clark on 9/27/2020
 */

val WeatherDataResponse.StringCelsius : String
    get() {
        val celsius = (this.main!!.temp - 273.15).roundToInt()
        return "$celsius°C"
    }

val WeatherDataResponse.IntCelsius : Int
    get() {
        return (this.main!!.temp - 273.15).roundToInt()
    }
