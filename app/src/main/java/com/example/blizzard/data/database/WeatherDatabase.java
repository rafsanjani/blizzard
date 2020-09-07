package com.example.blizzard.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.blizzard.data.entities.WeatherDataEntity;

@Database(entities = {WeatherDataEntity.class}, version = 2, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    private static WeatherDatabase instance = null;

    static Migration migration_ver1_ver2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'weather' ADD COLUMN 'isFavourite' INTEGER NOT NULL DEFAULT '0'");
        }
    };

    public static WeatherDatabase getInstance(Context context) {
        if (instance != null)
            return instance;

        return Room
                .databaseBuilder(context, WeatherDatabase.class, "weather")
                .addMigrations(migration_ver1_ver2)
                .build();
    }

    public abstract WeatherDao weatherDao();
}
