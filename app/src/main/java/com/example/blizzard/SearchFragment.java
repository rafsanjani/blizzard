package com.example.blizzard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.blizzard.model.OpenWeatherService;
import com.example.blizzard.model.Weather;
import com.example.blizzard.model.WeatherData;

import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    TextView tvCityTitle;
    TextView tvCityTemp;
    TextView tvCityHumidity;
    TextView tvCityDescription;
    private OpenWeatherService mService = new OpenWeatherService();

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

        // populating views with data
        populateData();

        view.findViewById(R.id.button_second).setOnClickListener(view1 -> NavHostFragment.findNavController(SearchFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));
    }

    private void populateData() {
        if (getArguments() != null){
            String cityName = SearchFragmentArgs.fromBundle(getArguments()).getCityName();
            Call<WeatherData> data = mService.getWeather(cityName);

            data.enqueue(new Callback<WeatherData>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                    if (response.isSuccessful()){
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

        Weather weather = weatherData.getWeather().get(0);
        tvCityDescription.setText(weather.getDescription());

    }

    private String conToCelsius(Double temp) {
        int celsius = (int) Math.round(temp - 273.15);
        return celsius + "°C";
    }
}