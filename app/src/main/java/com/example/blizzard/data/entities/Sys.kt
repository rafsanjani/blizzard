package com.example.blizzard.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Sys {
    @SerializedName("country")
    @Expose
    var country: String? = null
    override fun toString(): String {
        return "Sys{" +
                "country='" + country + '\'' +
                '}'
    }
}