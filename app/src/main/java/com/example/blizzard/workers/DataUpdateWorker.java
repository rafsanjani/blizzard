package com.example.blizzard.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.blizzard.Util.DatabaseInjector;
import com.example.blizzard.Util.NotificationHelper;
import com.example.blizzard.model.OpenWeatherService;
import com.example.blizzard.model.WeatherData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class DataUpdateWorker extends Worker {
    private static final String TAG = "DataUpdateWorker";
    private OpenWeatherService mWeatherService = new OpenWeatherService();

    public DataUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            List<WeatherData> data = getAllDataFromDb();

            if (data.size() > 0) {
                for (WeatherData curData : data) {
                    WeatherData serverData = getWeather(curData.getName());
                    if (serverData != null) {
                        if (curData.equals(serverData)) {
                            Log.d(TAG, "doWork: data received already exist in the db");
                        } else {
                            NotificationHelper.getInstance(getApplicationContext(), curData.getName());
                        }
                    }
                }
            }
            Log.d(TAG, "doWork: process was successful");
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "doWork error : ", e);
            return Result.failure();
        }
    }

    WeatherData getWeather(String cityName) {
        Call<WeatherData> weatherDataCall = mWeatherService.getWeatherByCityName(cityName);
        final WeatherData[] data = {null};

        weatherDataCall.enqueue(new Callback<WeatherData>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    data[0] = response.body();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.e(TAG, "server returned no data: ", t);
            }

        });

        return data[0];
    }

    List<WeatherData> getAllDataFromDb() {
        return DatabaseInjector.getDao(getApplicationContext()).getAll();
    }
}
