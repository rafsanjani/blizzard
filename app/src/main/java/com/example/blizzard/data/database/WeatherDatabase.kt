package com.example.blizzard.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.blizzard.data.entities.WeatherDataEntity

@Database(entities = [WeatherDataEntity::class], version = 3, exportSchema = true)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao?

    companion object {
        private val instance: WeatherDatabase? = null
        private var migration_ver1_ver2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'weather' ADD COLUMN 'isFavourite' INTEGER NOT NULL DEFAULT '0'")
            }
        }

        private val migration_ver2_ver3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // creating temporary db
                database.execSQL(
                        "CREATE TABLE IF NOT EXISTS weatherTmp(cityName TEXT PRIMARY KEY NOT NULL, country TEXT, temperature REAL, humidity INTEGER, description TEXT, windSpeed REAL, dt INTEGER, timeZone INTEGER, favourite INTEGER DEFAULT '0')")

                // copying data into temporary db
                database.execSQL("INSERT INTO weatherTmp(cityName, country, temperature, humidity , description, windSpeed, dt, timeZone, favourite) SELECT cityName, country, temperature, humidity , description, windSpeed, dt, timeZone, isFavourite FROM weather")

                // deleting old table
                database.execSQL("DROP TABLE weather")

                // rename weatherTmp to weather
                database.execSQL("ALTER TABLE weatherTmp RENAME TO weather")
            }
        }

        fun getInstance(context: Context?): WeatherDatabase {
            return instance
                    ?: Room
                            .databaseBuilder(context!!, WeatherDatabase::class.java, "weather")
                            .addMigrations(migration_ver1_ver2, migration_ver2_ver3)
                            .build()
        }
    }
}