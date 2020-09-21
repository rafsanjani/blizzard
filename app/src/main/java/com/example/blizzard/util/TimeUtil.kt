package com.example.blizzard.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kelvi on 8/4/2020
 */
class TimeUtil {
    var day: String? = null
    var timeAmPm: String? = null
    fun setTime(dateTimeStamp: Int, timeFromApi: Int) {
        val timeZoneString = getTimeZone(timeFromApi)
        val timeZone = TimeZone.getTimeZone(timeZoneString)
        val calendar = Calendar.getInstance(timeZone)
        val timeInstance = DateFormat.getTimeInstance(DateFormat.SHORT)
        timeInstance.timeZone = calendar.timeZone
        val date = Date(dateTimeStamp * 1000L)
        day = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
        timeAmPm = timeInstance.format(calendar.time)
    }

    private fun getTimeZone(timeFromApi: Int): String {
        val hour = timeFromApi / 3600
        return if (hour < 0) {
            "GMT$hour:00"
        } else {
            "GMT+$hour:00"
        }
    }

    fun getTime(): String {
        return "$day, $timeAmPm"
    }
}