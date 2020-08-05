
package com.example.blizzard.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherData {


    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("visibility")
    @Expose
    private int visibility;
    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("dt")
    @Expose
    private int dt;

    @SerializedName("timezone")
    @Expose
    private int timezone;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private int cod;
    @SerializedName("sys")
    @Expose
    private Sys sys;


    public List<Weather> getWeather() {
        return weather;
    }


    public String getBase() {
        return base;
    }


    public Main getMain() {
        return main;
    }


    public int getVisibility() {
        return visibility;
    }


    public int getDt() {
        return dt;
    }


    public int getTimezone() {
        return timezone;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public int getCod() {
        return cod;
    }


    public Wind getWind() {
        return wind;
    }

    public Sys getSys() {
        return sys;
    }
}
