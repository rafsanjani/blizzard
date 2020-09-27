package com.example.blizzard.model

/**
 * Created by kelvi on 8/3/2020
 */

//class OpenWeatherService {
//    private val mOpenWeatherApi: OpenWeatherApi
//    fun getWeather(cityName: String?): WeatherDataResponse {
//        return mOpenWeatherApi.getWeatherByCityName(cityName, ApiKeyHolder.API_KEY)
//    }
//
//    fun getWeather(latitude: Double?, longitude: Double?): WeatherDataResponse {
//        return mOpenWeatherApi.getWeatherByLongitudeLatitude(latitude, longitude, ApiKeyHolder.API_KEY)
//    }
//
//    companion object {
//        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
//    }
//
//    init {
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//        val client = OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .build()
//        mOpenWeatherApi = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(OpenWeatherApi::class.java)
//    }
//}

