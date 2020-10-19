package com.example.blizzard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sys (
    @SerializedName("country")
    @Expose
    var country: String? = null
)