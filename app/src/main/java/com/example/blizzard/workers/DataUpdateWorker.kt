package com.example.blizzard.workers

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.data.repository.BlizzardRepository
import com.example.blizzard.model.OpenWeatherService
import com.example.blizzard.model.WeatherDataResponse
import com.example.blizzard.util.BlizzardThread.Companion.instance
import com.example.blizzard.util.NotificationHelper.Companion.getInstance
import com.example.blizzard.util.TempConverter.kelToCelsius2
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs

class DataUpdateWorker(context: Context, workerParams: WorkerParameters) : ListenableWorker(context, workerParams) {
    private val repository: BlizzardRepository = BlizzardRepository(context)
    private var callback: Callback<WeatherDataResponse?>? = null
    private val blizzardThread = instance
    private val data: MutableList<WeatherDataEntity> = ArrayList()
    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { completer: CallbackToFutureAdapter.Completer<Result> ->
            allFromDb
            blizzardThread?.handler?.postDelayed({
                if (data.isEmpty()) {
                    Log.d(TAG, "doWork: No weather info in database")
                    completer.set(Result.success())
                }
                callback = object : Callback<WeatherDataResponse?> {
                    override fun onResponse(call: Call<WeatherDataResponse?>, response: Response<WeatherDataResponse?>) {
                        val currentWeather = response.body()
                        for (previousWeather in data) {
                            if (previousWeather.cityName == currentWeather?.name) {
                                val difference = abs(previousWeather.temperature!! - currentWeather.main!!.temp)
                                Log.d(TAG, "onResponse: Weather difference is $difference°C")
                                if (difference > 2) {
                                    Log.d(TAG, "onResponse: Weather Changes detected: Notifying")
                                    val notificationHelper = getInstance(applicationContext,
                                            previousWeather.cityName + ", " + previousWeather.country,
                                            kelToCelsius2(previousWeather.temperature!!),
                                            kelToCelsius2(currentWeather.main.temp))
                                    notificationHelper!!.createNotification()
                                    break
                                }
                            }
                        }
                        completer.set(Result.success())
                    }

                    override fun onFailure(call: Call<WeatherDataResponse?>, t: Throwable) {
                        Log.e(TAG, "onFailure: Error Fetching current Weather", t)
                        completer.set(Result.failure())
                    }
                }
                Log.d(TAG, "startWork: getting data from api")
                makeNetworkRequest()
            }, 5000)
            callback
        }
    }

    private fun makeNetworkRequest() {
        for (entity in data) {
            OpenWeatherService().getWeather(entity.cityName)!!.enqueue(callback!!)
        }
    }

    private val allFromDb: Unit
        get() {
            val weatherDataEntities = AtomicReference<List<WeatherDataEntity?>?>()
            CoroutineScope(IO).launch {
                weatherDataEntities.set(repository.getAll())
                Log.d(TAG, "getAllFromDb: done fetching")
                populate(weatherDataEntities.get())
            }
        }

    private fun populate(weatherDataEntities: List<WeatherDataEntity?>?) {
        data.addAll(weatherDataEntities as List<WeatherDataEntity>)
    }

    companion object {
        private const val TAG = "DataUpdateWorker"
    }

}


