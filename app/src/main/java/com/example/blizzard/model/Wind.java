
package com.example.blizzard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class Wind {

    @SerializedName("speed")
    @Expose
    private double speed;


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @NotNull
    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                '}';
    }
}
