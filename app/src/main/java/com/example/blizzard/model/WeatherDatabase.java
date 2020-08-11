package com.example.blizzard.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {WeatherData.class}, version = 1, exportSchema = false)
@TypeConverters({Wind.class, Weather.class, Sys.class, Main.class})
public abstract class WeatherDatabase extends RoomDatabase {

    public abstract WeatherDao getDao();
}
