package com.example.blizzard.model

import com.example.blizzard.data.entities.Sys
import com.example.blizzard.data.entities.Weather
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherDataResponse {
    @SerializedName("weather")
    @Expose
    val weather: List<Weather>? = null

    @SerializedName("main")
    @Expose
    val main: Main? = null

    @SerializedName("wind")
    @Expose
    var wind: Wind? = null

    @SerializedName("dt")
    @Expose
    var dt = 0

    @SerializedName("timezone")
    @Expose
    var timezone = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("sys")
    @Expose
    var sys: Sys? = null
    override fun toString(): String {
        return "WeatherData{" +
                "weather=" + weather.toString() +
                ", main=" + main.toString() +
                ", wind=" + wind.toString() +
                ", dt=" + dt +
                ", timezone=" + timezone +
                ", name='" + name + '\'' +
                ", sys=" + sys.toString() +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        return if (other !== this || other !== this.javaClass) {
            false
        } else {
            val guestWeatherDataResponse = other as WeatherDataResponse
            val guestWeather = guestWeatherDataResponse.weather!![0]
            val dbWeatherData = weather!![0]
            val guestWind = guestWeatherDataResponse.wind
            val dbWind = wind
            val guestMain = guestWeatherDataResponse.main
            val dbMain = main
            (guestWeather.description == dbWeatherData.description
                    && guestWeather.icon == dbWeatherData.icon
                    && guestWind!!.speed == dbWind!!.speed
                    && guestMain!!.humidity == dbMain!!.humidity
                    && guestMain.temp == dbMain.temp)
        }
    }

    override fun hashCode(): Int {
        var result = weather?.hashCode() ?: 0
        result = 31 * result + (main?.hashCode() ?: 0)
        result = 31 * result + (wind?.hashCode() ?: 0)
        result = 31 * result + dt
        result = 31 * result + timezone
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (sys?.hashCode() ?: 0)
        return result
    }
}