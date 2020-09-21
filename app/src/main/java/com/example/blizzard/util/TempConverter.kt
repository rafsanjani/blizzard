package com.example.blizzard.util

import kotlin.math.roundToInt

/**
 * Created by kelvin_clark on 8/19/2020
 */
object TempConverter {
    fun kelToCelsius(temp: Double): String {
        val celsius = (temp - 273.15).roundToInt()
        return "$celsius°C"
    }

    @JvmStatic
    fun kelToCelsius2(temp: Double): Int {
        return (temp - 273.15).roundToInt()
    }
}