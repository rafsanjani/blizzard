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
import com.example.blizzard.model.WeatherDataEntity;

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
            List<WeatherDataEntity> data = getAllDataFromDb();

            if (data.size() > 0) {
                for (WeatherDataEntity curData : data) {
                    WeatherDataEntity serverData = getWeather(curData.getCityName());
                    if (serverData != null) {
                        if (curData.equals(serverData)) {
                            Log.d(TAG, "doWork: data received already exist in the db");
                        } else {
                            NotificationHelper.getInstance(getApplicationContext(), curData.getCityName());
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

    WeatherDataEntity getWeather(String cityName) {
        Call<WeatherData> weatherDataCall = mWeatherService.getWeatherByCityName(cityName);
        final WeatherData[] data = {null};

        weatherDataCall.enqueue(new Callback<WeatherData>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body());
                    data[0] = response.body();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.e(TAG, "server returned no data: ", t);
            }

        });

        return data[0] != null ? new WeatherDataEntity(
                data[0].getName(),
                data[0].getMain().getTemp(),
                data[0].getMain().getHumidity(),
                data[0].getWeather().get(0).getDescription(),
                data[0].getWind().getSpeed()) : null;
    }

    List<WeatherDataEntity> getAllDataFromDb() {
        return DatabaseInjector.getDao(getApplicationContext()).getAll();
    }
}
