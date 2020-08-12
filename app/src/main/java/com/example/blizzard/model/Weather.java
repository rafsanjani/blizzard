
package com.example.blizzard.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Weather {


    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;


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

//    @TypeConverter
//    public static List<Weather> jsonToWeather(String json) {
//        Type listType = new TypeToken<List<Weather>>() {
//        }.getType();
//        return new Gson().fromJson(json, listType);
//    }
//
//    @TypeConverter
//    public static String WeatherToJson(List<Weather> weathers) {
//        Weather weather = weathers.get(0);
//        return new Gson().toJson(weather, Weather.class);
//    }

}
