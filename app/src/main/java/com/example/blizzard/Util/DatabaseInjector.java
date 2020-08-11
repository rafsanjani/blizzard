package com.example.blizzard.Util;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.blizzard.model.WeatherDao;
import com.example.blizzard.model.WeatherData;
import com.example.blizzard.model.WeatherDatabase;

public class DatabaseInjector {

    private DatabaseInjector() {
    }


    private static WeatherDatabase getInstance(Context context) {
        final String DATABASE_NAME = "weather_database";
        return
                Room.databaseBuilder(
                        context.getApplicationContext(),
                        WeatherDatabase.class,
                        DATABASE_NAME
                ).build();
    }

    public static WeatherDao getDao(Context context) {
        WeatherDatabase db = getInstance(context);
        return db.getDao();
    }
}
