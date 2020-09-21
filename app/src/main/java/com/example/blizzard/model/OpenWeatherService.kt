package com.example.blizzard.model;

import com.example.blizzard.data.api.OpenWeatherApi;
import com.example.blizzard.util.ApiKeyHolder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kelvi on 8/3/2020
 */
public class OpenWeatherService {

    private final static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private final OpenWeatherApi mOpenWeatherApi;

    public OpenWeatherService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        mOpenWeatherApi = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApi.class);
    }

    public Call<WeatherDataResponse> getWeather(String cityName) {
        return mOpenWeatherApi.getWeatherByCityName(cityName, ApiKeyHolder.API_KEY);
    }

    public Call<WeatherDataResponse> getWeather(Double latitude, Double longitude) {
        return mOpenWeatherApi.getWeatherByLongitudeLatitude(latitude, longitude, ApiKeyHolder.API_KEY);
    }

}
