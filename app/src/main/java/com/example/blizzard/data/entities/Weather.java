
package com.example.blizzard.data.entities;

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

}
