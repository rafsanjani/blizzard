package com.example.blizzard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kelvi on 8/3/2020
 */
public class WeatherData {
    private int id;

    @SerializedName("name")
    private String cityName;

    private String country;

    @SerializedName("main")
    private String state;

    private String description;

    private String icon;

    @SerializedName("temp")
    private float temperature;

    private int humidity;

    public WeatherData(int id, String cityName, String country, String state, String description, String icon, float temperature, int humidity) {
        this.id = id;
        this.cityName = cityName;
        this.country = country;
        this.state = state;
        this.description = description;
        this.icon = icon;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}

