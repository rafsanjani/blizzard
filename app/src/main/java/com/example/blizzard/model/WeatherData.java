package com.example.blizzard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kelvi on 8/3/2020
 */
public class WeatherData {
    private int id;

    @SerializedName("main")
    private String state;

    private String description;

    private String icon;

    @SerializedName("name")
    private String cityTitle;

    private String humidity;

    @SerializedName("temp")
    private String temperature;


}
