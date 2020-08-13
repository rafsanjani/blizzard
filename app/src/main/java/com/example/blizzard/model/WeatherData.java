
package com.example.blizzard.model;

import androidx.annotation.Nullable;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;


public class WeatherData {


    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;


    @SerializedName("main")
    @Expose
    private Main main;


    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("dt")
    @Expose
    private int dt;

    @SerializedName("timezone")
    @Expose
    private int timezone;

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("sys")
    @Expose
    private Sys sys;


    public List<Weather> getWeather() {
        return weather;
    }


    public Main getMain() {
        return main;
    }


    public int getDt() {
        return dt;
    }


    public int getTimezone() {
        return timezone;
    }


    public String getName() {
        return name;
    }


    public Wind getWind() {
        return wind;
    }

    public Sys getSys() {
        return sys;
    }


    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }


    public void setMain(Main main) {
        this.main = main;
    }


    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setSys(Sys sys) {
        this.sys = sys;
    }

    @NotNull
    @Override
    public String toString() {
        return "WeatherData{" +
                "weather=" + weather.toString() +
                ", main=" + main.toString() +
                ", wind=" + wind.toString() +
                ", dt=" + dt +
                ", timezone=" + timezone +
                ", name='" + name + '\'' +
                ", sys=" + sys.toString() +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != this || obj != this.getClass()) {
            return false;
        } else {
            WeatherData guestWeatherData = (WeatherData) obj;


            Weather guestWeather = guestWeatherData.getWeather().get(0);
            Weather dbWeatherData = getWeather().get(0);

            Wind guestWind = guestWeatherData.getWind();
            Wind dbWind = getWind();

            Main guestMain = guestWeatherData.getMain();
            Main dbMain = getMain();

            return
                    (guestWeather.getDescription().equals(dbWeatherData.getDescription()))
                            && (guestWeather.getIcon().equals(dbWeatherData.getIcon()))
                            && (guestWind.getSpeed() == dbWind.getSpeed())
                            && (guestMain.getHumidity() == dbMain.getHumidity())
                            && (guestMain.getTemp() == dbMain.getTemp());
        }
    }
}
