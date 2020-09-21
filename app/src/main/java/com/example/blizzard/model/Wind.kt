package com.example.blizzard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Wind {
    @SerializedName("speed")
    @Expose
    var speed = 0.0
    override fun toString(): String {
        return "Wind{" +
                "speed=" + speed +
                '}'
    }
}