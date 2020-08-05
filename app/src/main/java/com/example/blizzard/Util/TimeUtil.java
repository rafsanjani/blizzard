package com.example.blizzard.Util;

import android.util.Log;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by kelvi on 8/4/2020
 */
public class TimeUtil {
    String day;
    String time;


    public TimeUtil() {

    }


    public void setTime(int timeFromApi){

        Date date = new Date(timeFromApi * 1000L);

        day = new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
        Log.i("clarkDate", day);

        time = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);

    }

    public String getTime() {

        return day + ", " + time;
    }


}