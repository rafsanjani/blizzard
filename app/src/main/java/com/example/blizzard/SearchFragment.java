package com.example.blizzard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.blizzard.model.OpenWeatherService;
import com.example.blizzard.model.Weather;
import com.example.blizzard.model.WeatherData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    TextView tvCityTitle;
    TextView tvCityTemp;
    TextView tvCityHumidity;
    TextView tvCityDescription;
    ImageView IvWeatherImage;
    private OpenWeatherService mService = new OpenWeatherService();
    TextView tvCityWind;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCityTitle = view.findViewById(R.id.city_title);
        tvCityDescription = view.findViewById(R.id.weather_description_textView);
        tvCityHumidity = view.findViewById(R.id.city_humidity);
        tvCityTemp = view.findViewById(R.id.city_temp);
        IvWeatherImage = view.findViewById(R.id.weather_icon_imageView);
        tvCityWind = view.findViewById(R.id.city_wind_speed);

        // populating views with data
        populateData();

        view.findViewById(R.id.button_second).setOnClickListener(view1 -> NavHostFragment
                .findNavController(SearchFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));
    }

    private void populateData() {
        if (getArguments() != null) {
            String cityName = SearchFragmentArgs.fromBundle(getArguments()).getCityName();
            Call<WeatherData> data = mService.getWeather(cityName);

            data.enqueue(new Callback<WeatherData>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                    if (response.isSuccessful()) {
                        WeatherData weatherData = response.body();
                        assert weatherData != null;
                        insertDataIntoViews(weatherData);
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<WeatherData> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }
    }

    private void insertDataIntoViews(WeatherData weatherData) {
        tvCityTitle.setText(weatherData.getName());

        Double temp = weatherData.getMain().getTemp();
        tvCityTemp.setText(conToCelsius(temp));

        String humidity = weatherData.getMain().getHumidity() + "%";
        tvCityHumidity.setText(humidity);

        Double wind = weatherData.getWind().getSpeed();
        tvCityWind.setText(windDirection(weatherData, wind));


        Weather weather = weatherData.getWeather().get(0);
        tvCityDescription.setText(weather.getDescription());

        LoadImage(weather.getIcon());
    }


    private void LoadImage(String iconId) {
        String url = String.format("http://openweathermap.org/img/wn/%s@4x.png", iconId);

        Glide.with(requireView())
                .load(url)
                .error(R.drawable.ic_cloud)
                .into(IvWeatherImage);
    }

    private String conToCelsius(Double temp) {
        int celsius = (int) Math.round(temp - 273.15);
        return celsius + "°C";
    }

    private String windDirection(WeatherData weatherData, Double wind) {
        Double windDeg = weatherData.getWind().getDeg();
        String direction = "";
        if(windDeg == 0) {
            return "N";
        }else if (windDeg > 0 && windDeg < 90) {
            direction = "NE";
        }else if (windDeg == 90) {
            direction = "E";
        }else if (windDeg > 90 && windDeg < 270) {
            direction = "SW";
        }else if (windDeg == 270) {
            direction = "W";
        }else if (windDeg > 270 && windDeg < 360) {
            direction = "NW";
        }
        return direction+ " " + wind + "mph";
    }
}