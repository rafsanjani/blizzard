package com.example.blizzard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.blizzard.data.database.WeatherMapper;
import com.example.blizzard.data.entities.Weather;
import com.example.blizzard.data.entities.WeatherDataEntity;
import com.example.blizzard.model.WeatherDataResponse;
import com.example.blizzard.util.TimeUtil;
import com.example.blizzard.viewmodel.BlizzardViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class SearchFragment extends Fragment {
    TextView tvCityTitle;
    TextView tvCityTemp;
    TextView tvCityHumidity;
    TextView tvCityDescription;
    TextView tvCityWindSpeed;
    TextView tvTime;
    ImageView IvWeatherImage;
    ProgressBar dataLoading;
    private final TimeUtil mTimeUtil = new TimeUtil();
    private BlizzardViewModel mBlizzardViewModel;
    private static final String TAG = "SearchFragment";

    public static final String CITY_NAME = "com.example.blizzard.cityName";
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlizzardViewModel = new ViewModelProvider(requireActivity()).get(BlizzardViewModel.class);
//        mBlizzardViewModel.init();

        assert getArguments() != null;
        String cityName = SearchFragmentArgs.fromBundle(getArguments()).getCityName();
        mBlizzardViewModel.getWeather(cityName);

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

        observeViewModels(view);
    }

    private void observeViewModels(View view) {
        mBlizzardViewModel.getWeatherLiveData().observe(getViewLifecycleOwner(), weatherData -> {
            if (weatherData != null) {
                saveToDb(weatherData);
                mTimeUtil.setTime(weatherData.getDt(), weatherData.getTimezone());
                insertDataIntoViews(weatherData);
            } else {
                Snackbar.make(view, R.string.error_getting_data, Snackbar.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    private void saveToDb(WeatherDataResponse weatherDataResponse) {
        executor.execute(() -> {
            WeatherDataEntity entity = WeatherMapper.mapToEntity(weatherDataResponse);
            mBlizzardViewModel.saveWeather(entity);
        });
    }


    private void findViews(@NonNull View view) {
        tvCityTitle = view.findViewById(R.id.tv_cityName);
        tvCityDescription = view.findViewById(R.id.tv_weatherDescription);
        tvCityHumidity = view.findViewById(R.id.tv_humidityValue);
        tvCityTemp = view.findViewById(R.id.tv_tempValue);
        tvCityWindSpeed = view.findViewById(R.id.tv_windSpeed);
        tvTime = view.findViewById(R.id.tv_dayTime);
        IvWeatherImage = view.findViewById(R.id.weather_icon);
        dataLoading = view.findViewById(R.id.data_loading);
    }

    private void insertDataIntoViews(WeatherDataResponse weatherDataResponse) {
        String cityName = weatherDataResponse.getName() + ", " + weatherDataResponse.getSys().getCountry();
        tvCityTitle.setText(cityName);

        Double temp = weatherDataResponse.getMain().getTemp();
        tvCityTemp.setText(conToCelsius(temp));

        String humidity = weatherDataResponse.getMain().getHumidity() + "%";
        tvCityHumidity.setText(humidity);

        Weather weather = weatherDataResponse.getWeather().get(0);
        tvCityDescription.setText(weather.getDescription());

        LoadImage(weather.getIcon());

        String windSpeed = weatherDataResponse.getWind().getSpeed() + " m/s";

        tvCityWindSpeed.setText(windSpeed);

        tvTime.setText(mTimeUtil.getTime());

        dataLoading.setVisibility(View.INVISIBLE);
        makeVisible();

    }

    private void makeVisible() {
        tvCityTitle.setVisibility(View.VISIBLE);
        tvCityDescription.setVisibility(View.VISIBLE);
        tvCityHumidity.setVisibility(View.VISIBLE);
        tvCityTemp.setVisibility(View.VISIBLE);
        tvCityWindSpeed.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);
        IvWeatherImage.setVisibility(View.VISIBLE);
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
}