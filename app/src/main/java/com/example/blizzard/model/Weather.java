
package com.example.blizzard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public String toString() {
        return "Weather{" +
                "description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
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
