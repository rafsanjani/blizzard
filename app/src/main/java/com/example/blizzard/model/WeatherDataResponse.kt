package com.example.blizzard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherDataResponse(
    @SerializedName("weather")
    @Expose
    val weather: List<Weather>? = null,

    @SerializedName("main")
    @Expose
    val main: Main? = null,

    @SerializedName("wind")
    @Expose
    var wind: Wind? = null,

    @SerializedName("dt")
    @Expose
    var dt :Int? = null,

    @SerializedName("timezone")
    @Expose
    var timezone : Int? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("sys")
    @Expose
    var sys: Sys? = null
)