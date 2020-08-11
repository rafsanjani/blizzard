
package com.example.blizzard.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    @Expose
    private double speed;
    @SerializedName("deg")
    @Expose
    private int deg;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    @TypeConverter
    public String windToJson(Wind wind) {

        return new Gson().toJson(wind, Wind.class);
    }

    @TypeConverter
    public Wind jsonToWind(String json) {

        return new Gson().fromJson(json, Wind.class);
    }

}
