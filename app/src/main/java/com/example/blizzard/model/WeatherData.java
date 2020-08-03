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

    public WeatherData(int id, String state, String description, String icon, String cityTitle, String humidity, String temperature) {
        this.id = id;
        this.state = state;
        this.description = description;
        this.icon = icon;
        this.cityTitle = cityTitle;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
