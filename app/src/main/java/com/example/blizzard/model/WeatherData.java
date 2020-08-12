
package com.example.blizzard.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class WeatherData {


    @SerializedName("weather")
    @Expose
    @TypeConverters(Weather.class)
    private List<Weather> weather = null;


    @SerializedName("main")
    @Expose
    @TypeConverters(Main.class)
    private Main main;


    @SerializedName("wind")
    @Expose
    @TypeConverters(Wind.class)
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
    @TypeConverters(Sys.class)
    private Sys sys;


    @PrimaryKey(autoGenerate = true)
    private int uuid;


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

    public int getUuid() {
        return uuid;
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

    public void setUuid(int uuid) {
        this.uuid = uuid;
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
