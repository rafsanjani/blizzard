
package com.example.blizzard.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {


    @SerializedName("country")
    @Expose
    private String country;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @TypeConverter
    public String SysToJson(Sys sys) {

        return new Gson().toJson(sys, Sys.class);
    }

    @TypeConverter
    public Sys jsonToSys(String json) {

        return new Gson().fromJson(json, Sys.class);
    }
}
