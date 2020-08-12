
package com.example.blizzard.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    @Expose
    private double temp;

    @SerializedName("humidity")
    @Expose
    private int humidity;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    @TypeConverter
    public String windToJson(Main main) {

        return new Gson().toJson(main, Main.class);
    }

    @TypeConverter
    public Main windToJson(String json) {

        return new Gson().fromJson(json, Main.class);
    }
}
