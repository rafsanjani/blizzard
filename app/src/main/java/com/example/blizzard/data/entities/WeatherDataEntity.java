package com.example.blizzard.data.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "weather")
public class WeatherDataEntity {
    @NonNull
    @PrimaryKey()
    private String cityName;
    private double temperature;
    private int humidity;
    private String description;
    private double windSpeed;
    private String country;


    public WeatherDataEntity(@NotNull String cityName, String country, double temperature, int humidity, String description, double windSpeed) {
        this.cityName = cityName;
        this.country = country;
        this.description = description;
        this.humidity = humidity;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    @NotNull
    public String getCityName() {
        return cityName;
    }

    public void setCityName(@NotNull String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        WeatherDataEntity guestEntity = (WeatherDataEntity) obj;


        return this.cityName.equals(guestEntity.getCityName())
                && (this.description.equals(guestEntity.getDescription()))
                && (this.humidity == guestEntity.getHumidity())
                && (this.temperature == guestEntity.getTemperature())
                && (this.windSpeed == guestEntity.getWindSpeed());
    }
}
