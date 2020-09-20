
package com.example.blizzard.data.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public String toString() {
        return "Sys{" +
                "country='" + country + '\'' +
                '}';
    }
}
