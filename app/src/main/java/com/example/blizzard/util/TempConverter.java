package com.example.blizzard.util;

/**
 * Created by kelvin_clark on 8/19/2020
 */
public  class TempConverter {
    public static String kelToCelsius(Double temp) {
        int celsius = (int) Math.round(temp - 273.15);
        return celsius + "°C";
    }


    public static int kelToCelsius2(Double temp){
        return (int) Math.round(temp - 273.15);
    }
}
