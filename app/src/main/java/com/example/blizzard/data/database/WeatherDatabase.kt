package com.example.blizzard.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.blizzard.data.entities.WeatherDataEntity

@Database(entities = [WeatherDataEntity::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao?

    companion object {
        private val instance: WeatherDatabase? = null
        private var migration_ver1_ver2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'weather' ADD COLUMN 'isFavourite' INTEGER NOT NULL DEFAULT '0'")
            }
        }

        fun getInstance(context: Context?): WeatherDatabase {
            return instance
                    ?: Room
                            .databaseBuilder(context!!, WeatherDatabase::class.java, "weather")
                            .addMigrations(migration_ver1_ver2)
                            .build()
        }
    }
}