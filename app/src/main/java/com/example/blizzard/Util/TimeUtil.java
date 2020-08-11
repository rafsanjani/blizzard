package com.example.blizzard.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by kelvi on 8/4/2020
 */
public class TimeUtil {
    String day;
    String time;


    public TimeUtil() {

    }


    public void setTime(int dateTimeStamp, int timeFromApi) {
        String timeZoneString = getTimeZone(timeFromApi);

        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);

        Calendar calendar = Calendar.getInstance(timeZone);

        DateFormat timeInstance = DateFormat.getTimeInstance(DateFormat.SHORT);
        timeInstance.setTimeZone(calendar.getTimeZone());

        Date date = new Date(dateTimeStamp * 1000L);

        day = new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);

        time = timeInstance.format(calendar.getTime());

    }

    private String getTimeZone(int timeFromApi) {
        int hour = timeFromApi / 3600;

        if (hour < 0) {
            return "GMT" + hour + ":00";
        } else {
            return "GMT+" + hour + ":00";
        }
    }

    public String getTime() {

        return day + ", " + time;
    }


}