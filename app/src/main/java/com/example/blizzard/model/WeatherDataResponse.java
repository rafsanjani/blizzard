
package com.example.blizzard.model;

import androidx.annotation.Nullable;

import com.example.blizzard.data.entities.Sys;
import com.example.blizzard.data.entities.Weather;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class WeatherDataResponse {


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
    private int timeZone;

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


    public int getTimeZone() {
        return timeZone;
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


    @NotNull
    @Override
    public String toString() {
        return "WeatherData{" +
                "weather=" + weather.toString() +
                ", main=" + main.toString() +
                ", wind=" + wind.toString() +
                ", dt=" + dt +
                ", timezone=" + timeZone +
                ", name='" + name + '\'' +
                ", sys=" + sys.toString() +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != this || obj != this.getClass()) {
            return false;
        } else {
            WeatherDataResponse guestWeatherDataResponse = (WeatherDataResponse) obj;


            Weather guestWeather = guestWeatherDataResponse.getWeather().get(0);
            Weather dbWeatherData = getWeather().get(0);

            Wind guestWind = guestWeatherDataResponse.getWind();
            Wind dbWind = getWind();

            Main guestMain = guestWeatherDataResponse.getMain();
            Main dbMain = getMain();

            return
                    (guestWeather.getDescription().equals(dbWeatherData.getDescription()))
                            && (guestWeather.getIcon().equals(dbWeatherData.getIcon()))
                            && (guestWind.getSpeed() == dbWind.getSpeed())
                            && (guestMain.getHumidity() == dbMain.getHumidity())
                            && (guestMain.getTemp() == dbMain.getTemp());
        }
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }
}
