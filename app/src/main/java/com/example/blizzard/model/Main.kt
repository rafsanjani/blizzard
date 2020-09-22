package com.example.blizzard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Main {
    @SerializedName("temp")
    @Expose
    var temp = 0.0

    @SerializedName("humidity")
    @Expose
    var humidity = 0
    override fun toString(): String {
        return "Main{" +
                "temp=" + temp +
                ", humidity=" + humidity +
                '}'
    } //    @TypeConverter
    //    public String windToJson(Main main) {
    //
    //        return new Gson().toJson(main, Main.class);
    //    }
    //
    //    @TypeConverter
    //    public Main windToJson(String json) {
    //
    //        return new Gson().fromJson(json, Main.class);
    //    }
}