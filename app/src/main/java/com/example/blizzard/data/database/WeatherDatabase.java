package com.example.blizzard.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.blizzard.data.entities.WeatherDataEntity;

@Database(entities = {WeatherDataEntity.class}, version = 2, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    private static WeatherDatabase instance = null;

    public static WeatherDatabase getInstance(Context context) {
        if (instance != null)
            return instance;

        return Room
                .databaseBuilder(context, WeatherDatabase.class, "weather")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public abstract WeatherDao weatherDao();
}
