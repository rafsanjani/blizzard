package com.example.blizzard.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.blizzard.data.entities.extensions.IntCelsius
import com.example.blizzard.data.repository.BlizzardRepository
import com.example.blizzard.model.extensions.IntCelsius
import com.example.blizzard.util.NotificationHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.abs

class DataUpdateWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val repository: BlizzardRepository = BlizzardRepository(context)

    companion object {
        private const val TAG = "DataUpdateWorker"
    }

    override suspend fun doWork(): Result = coroutineScope {
        val job = async{
            Log.i(TAG, "doWork: Fetching data from db")
            repository.getAll()
        }

        job.await().let {weatherDataEntityList ->
            weatherDataEntityList?.forEach{weatherDataEntity ->
                Log.i(TAG, "doWork: Fetching data from api")
                val onlineData = repository.getWeather(weatherDataEntity!!.cityName)
                if (weatherDataEntity.cityName == onlineData.name) {
                    Log.i(TAG, "doWork: Comparing data")
                    val difference = abs(weatherDataEntity.temperature!! - onlineData.main!!.temp)
                    Log.d(TAG, "onResponse: Weather difference is $difference°C")
                    if (difference > 2) {
                        Log.d(TAG, "onResponse: Weather Changes detected: Notifying")
                        val notificationHelper = NotificationHelper.getInstance(applicationContext,
                                weatherDataEntity.cityName + ", " + weatherDataEntity.country,
                                weatherDataEntity.IntCelsius,
                                onlineData.IntCelsius)
                        notificationHelper!!.createNotification()
                    }
                }
            }
            Log.i(TAG, "doWork: Work completed successfully")
            Result.success()
        }
    }

}


