package com.example.blizzard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.blizzard.HomeFragmentDirections.ActionFirstFragmentToSecondFragment;
import com.example.blizzard.Util.TimeUtil;
import com.example.blizzard.model.OpenWeatherService;
import com.example.blizzard.model.Weather;
import com.example.blizzard.model.WeatherData;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class HomeFragment extends Fragment {

    public static final int TURN_ON_LOCATION = 3030;
    TextView tvCityTitle;
    TextView tvCityTemp;
    TextView tvCityHumidity;
    TextView tvCityDescription;
    TextView tvCityWindSpeed;
    TextView tvTime;
    ImageView IvWeatherImage;
    TextInputEditText searchBox;
    ProgressBar dataLoading;
    private OpenWeatherService mWeatherService = new OpenWeatherService();
    private TimeUtil mTimeUtil = new TimeUtil();
    private static final int LOCATION_REQUEST_CODE = 123;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        checkLocationPermission();

        view.findViewById(R.id.search_btn).setOnClickListener(view1 -> {
            if (Objects.requireNonNull(searchBox.getText()).toString().isEmpty()) {
                searchBox.setError("Enter city name");
            } else {
                String cityName = searchBox.getText().toString();
                ActionFirstFragmentToSecondFragment action = HomeFragmentDirections.actionFirstFragmentToSecondFragment(cityName);
                Navigation.findNavController(view1).navigate(action);
            }
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
        searchBox = view.findViewById(R.id.et_cityName);
        dataLoading = view.findViewById(R.id.data_loading);
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.permissionRationalTitle)
                        .setMessage(R.string.permissionRationalMessage)
                        .setNegativeButton("No", (dialogInterface, i) -> notifyFragment(false))
                        .setPositiveButton("Ok, ask again", (dialogInterface, i) -> requestLocationPermission())
                        .show();
            } else {
                requestLocationPermission();
            }
        } else {
            notifyFragment(true);
        }

    }

    private void requestLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(requireActivity(), permissions, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                notifyFragment(true);
            } else {
                notifyFragment(false);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void notifyFragment(Boolean flag) {
        if (flag) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);

            SettingsClient settingsClient = LocationServices.getSettingsClient(requireActivity());


            Task<LocationSettingsResponse> locationResponse = settingsClient.checkLocationSettings(builder.build());

            locationResponse.addOnSuccessListener(locationSettingsResponse -> getUserLocation())
            .addOnFailureListener(e -> {
                if (e instanceof ResolvableApiException) {

                    try {

                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(requireActivity(), TURN_ON_LOCATION);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TURN_ON_LOCATION ){
            getUserLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        ArrayList<Double> lat_long = new ArrayList<>();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null){
                        lat_long.add(location.getLatitude());
                        lat_long.add(location.getLongitude());
                        locationGranted(lat_long);
                    }
                }).addOnFailureListener(requireActivity(), Throwable::getCause);
    }

    public void locationGranted(ArrayList<Double> lat_long) {
        if (lat_long.size() > 0){
            getWeatherData(lat_long);
        }

    }

    private void getWeatherData(ArrayList<Double> lat_long) {
        Double latitude = lat_long.get(0);
        Double longitude = lat_long.get(1);


        Call<WeatherData> data = mWeatherService.getWeatherByLongitudeLatitude(latitude, longitude);

        data.enqueue(new Callback<WeatherData>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    WeatherData weatherData = response.body();

                    assert weatherData != null;
                    mTimeUtil.setTime(weatherData.getDt(), weatherData.getTimezone());
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

    private void insertDataIntoViews(WeatherData weatherData) {


        String cityName = weatherData.getName() + ", " + weatherData.getSys().getCountry();
        tvCityTitle.setText(cityName);

        Double temp = weatherData.getMain().getTemp();
        tvCityTemp.setText(conToCelsius(temp));

        String humidity = weatherData.getMain().getHumidity() + "%";
        tvCityHumidity.setText(humidity);

        Weather weather = weatherData.getWeather().get(0);
        tvCityDescription.setText(weather.getDescription());

        LoadImage(weather.getIcon());

        String windSpeed = weatherData.getWind().getSpeed() + " m/s";

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