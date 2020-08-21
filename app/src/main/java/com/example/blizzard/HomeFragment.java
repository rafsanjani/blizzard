package com.example.blizzard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AndroidException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.blizzard.data.database.WeatherMapper;
import com.example.blizzard.data.entities.Weather;
import com.example.blizzard.data.entities.WeatherDataEntity;
import com.example.blizzard.model.WeatherDataResponse;
import com.example.blizzard.util.CheckNetworkUtil;
import com.example.blizzard.util.TempConverter;
import com.example.blizzard.util.TimeUtil;
import com.example.blizzard.viewmodel.BlizzardViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    public static final int ENABLE_LOCATION_HARDWARE = 3030;
    TextView tvCityTitle;
    TextView tvCityTemp;
    TextView tvCityHumidity;
    TextView tvCityDescription;
    TextView tvCityWindSpeed;
    TextView tvTime;
    ImageView ivWeatherImage;
    TextInputEditText searchBox;
    ProgressBar progressBar;
    Button btnSearch;
    private final TimeUtil mTimeUtil = new TimeUtil();
    private static final int LOCATION_REQUEST_CODE = 123;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationUpdatesCallback;
    private boolean mIsNetworkAvailable;
    private BlizzardViewModel mBlizzardViewModel;
    private TextInputLayout etContainer;
    private Button btnCurLocation;
    private FloatingActionButton fabSearch;
    private Boolean curLocIsVisible = true;
    private Animation slideRight;
    private Animation slideLeft;
    public static final String CITY_NAME = "com.example.blizzard.cityName";
    private View view;
    private Boolean searchByCityName = false;
    private ImageView ivNoInternet;
    private TextView tvHumidityTitle;
    private TextView tvWindTitle;
    private TextView tvNoInternet;
    private ImageView ivFavourite;
    Boolean isClicked = false;
    String cityName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlizzardViewModel = new ViewModelProvider(requireActivity()).get(BlizzardViewModel.class);


        slideRight = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right);
        slideLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_left);

        mLocationUpdatesCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "onLocationResult: Periodic Location Callback Triggered. Stopping Updates");
                stopLocationUpdates();
                Location location = locationResult.getLastLocation();
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                mBlizzardViewModel.getWeather(latitude, longitude);
            }


            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        CheckNetworkUtil mCheckNetworkUtil = new CheckNetworkUtil(getActivity());
        mIsNetworkAvailable = mCheckNetworkUtil.isNetworkAvailable();
        if (!mIsNetworkAvailable)
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
        initializeViews(view);

        Bundle bundle = this.getArguments();

        if (bundle == null) {
            ensureLocationIsEnabled();
        } else {
            String cityName = bundle.getString(HomeFragment.CITY_NAME);
            mBlizzardViewModel.getWeather(cityName);
            observeWeatherChanges();
        }


        btnSearch.setOnClickListener(view1 -> {
            //Hide the Keyboard when search button is clicked
            InputMethodManager inputMethodManager = (InputMethodManager)
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            mIsNetworkAvailable = mCheckNetworkUtil.isNetworkAvailable();
            if (Objects.requireNonNull(searchBox.getText()).toString().isEmpty()) {
                searchBox.setError("Enter city name");
            } else if (!mIsNetworkAvailable) {
                searchBox.setError(getString(R.string.no_internet));
            } else {
                String searchText = searchBox.getText().toString();
                searchByCityName = true;
                mBlizzardViewModel.getWeather(searchText);
                observeWeatherChanges();
                animateViews();
            }
        });

        btnCurLocation.setOnClickListener(view12 -> {
            ensureLocationIsEnabled();
            reverseViewAnim();
        });

        fabSearch.setOnClickListener(view13 -> reverseViewAnimToInit());

        ivFavourite.setOnClickListener(view14 -> {
            if (isClicked) {
                isClicked = false;
                LoadImage(R.drawable.ic_favorite, ivFavourite);
                updateIsFavourite(false);
            } else {
                isClicked = true;
                LoadImage(R.drawable.ic_favorite_filed, ivFavourite);
                updateIsFavourite(true);
            }
        });
    }

    private void updateIsFavourite(boolean b) {
        if (cityName != null) {
            Executors.newSingleThreadExecutor()
                    .execute(() -> {
                        WeatherDataEntity weatherDataEntity = mBlizzardViewModel.getWeatherByCityName(cityName);
                        weatherDataEntity.setFavourite(b);
                        mBlizzardViewModel.updateWeatherData(weatherDataEntity);
                    });
        }
    }

    private void reverseViewAnimToInit() {
        if (!curLocIsVisible) {
            fabSearch.startAnimation(slideRight);
            etContainer.startAnimation(slideLeft);
            btnSearch.startAnimation(slideLeft);

            fabSearch.setVisibility(View.INVISIBLE);
            fabSearch.setClickable(false);
            etContainer.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.VISIBLE);
            btnSearch.setClickable(true);
            curLocIsVisible = true;
        } else {
            fabSearch.startAnimation(slideRight);
            btnCurLocation.startAnimation(slideRight);
            etContainer.startAnimation(slideLeft);
            btnSearch.startAnimation(slideLeft);

            fabSearch.setVisibility(View.INVISIBLE);
            fabSearch.setClickable(false);
            btnCurLocation.setVisibility(View.INVISIBLE);
            btnCurLocation.setClickable(false);
            etContainer.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.VISIBLE);
            btnSearch.setClickable(true);
            curLocIsVisible = true;
        }
    }

    private void reverseViewAnim() {
        curLocIsVisible = false;
        btnCurLocation.startAnimation(slideRight);
        btnCurLocation.setVisibility(View.INVISIBLE);
        btnCurLocation.setClickable(false);
    }

    private void animateViews() {
        etContainer.startAnimation(slideRight);
        btnSearch.startAnimation(slideRight);
        btnCurLocation.startAnimation(slideLeft);
        fabSearch.startAnimation(slideLeft);


        etContainer.setVisibility(View.INVISIBLE);
        btnSearch.setVisibility(View.INVISIBLE);
        btnSearch.setClickable(false);
        btnCurLocation.setVisibility(View.VISIBLE);
        btnCurLocation.setClickable(true);
        fabSearch.setVisibility(View.VISIBLE);
        fabSearch.setClickable(true);


    }


    private void initializeViews(@NonNull View view) {
        btnSearch = view.findViewById(R.id.search_btn);
        tvCityTitle = view.findViewById(R.id.tv_cityName);
        tvCityDescription = view.findViewById(R.id.tv_weatherDescription);
        tvCityHumidity = view.findViewById(R.id.tv_humidityValue);
        tvCityTemp = view.findViewById(R.id.tv_tempValue);
        tvCityWindSpeed = view.findViewById(R.id.tv_windSpeed);
        tvTime = view.findViewById(R.id.tv_dayTime);
        ivWeatherImage = view.findViewById(R.id.weather_icon);
        searchBox = view.findViewById(R.id.et_cityName);
        progressBar = view.findViewById(R.id.data_loading);
        etContainer = view.findViewById(R.id.etLayoutContainer);
        btnCurLocation = view.findViewById(R.id.btn_current_location);
        fabSearch = view.findViewById(R.id.fab_search);
        ivNoInternet = view.findViewById(R.id.iv_no_internet);
        tvHumidityTitle = view.findViewById(R.id.tv_humidityTitle);
        tvWindTitle = view.findViewById(R.id.tv_windTitle);
        tvNoInternet = view.findViewById(R.id.tv_no_internet);
        ivFavourite = view.findViewById(R.id.iv_favourite);
    }

    public void makeViewsInvisible() {
        tvCityTitle.setVisibility(View.INVISIBLE);
        tvCityDescription.setVisibility(View.INVISIBLE);
        tvCityHumidity.setVisibility(View.INVISIBLE);
        tvCityTemp.setVisibility(View.INVISIBLE);
        tvCityWindSpeed.setVisibility(View.INVISIBLE);
        tvTime.setVisibility(View.INVISIBLE);
        ivWeatherImage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        tvHumidityTitle.setVisibility(View.INVISIBLE);
        tvWindTitle.setVisibility(View.INVISIBLE);
    }


    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.permissionRationalTitle)
                        .setMessage(R.string.permissionRationalMessage)
                        .setNegativeButton("No", (dialogInterface, i) -> ensureLocationIsEnabled())
                        .setPositiveButton("Ok, ask again", (dialogInterface, i) -> requestLocationPermission())
                        .show();
            } else {
                Log.d(TAG, "checkLocationPermission: " + "Requesting Location Permission Normally");
                requestLocationPermission();
            }
        } else {
            //location permission has been granted, we can proceed and obtain the user's location
            Log.d(TAG, "checkLocationPermission: Permission Granted: Getting User Location");
            getUserLocation();
        }
    }

    private void requestLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissions(permissions, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                Log.d(TAG, "onRequestPermissionsResult: Location Permission Granted, Requesting User Location");
                getUserLocation();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: Location Permission Denied, Quitting");
                //permission rejected
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void ensureLocationIsEnabled() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(requireActivity());


        Task<LocationSettingsResponse> locationResponse = settingsClient.checkLocationSettings(builder.build());
        locationResponse.addOnCompleteListener(task -> {
            try {
                task.getResult(ApiException.class);
                Log.d(TAG, "ensureLocationIsEnabled: Location Hardware previously enabled: Checking for permissions");

                //ask for location request
                checkLocationPermission();

            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.d(TAG, "ensureLocationIsEnabled: Location Hardware disabled: Asking user to enable");
                        ResolvableApiException resolvable = (ResolvableApiException) exception;
                        try {
                            startIntentSenderForResult(resolvable.getResolution().getIntentSender(), ENABLE_LOCATION_HARDWARE,
                                    null, 0, 0, 0, null);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG, "ensureLocationIsEnabled: Location Hardware disabled: Unable to toggle it on. Quitting");
                        //settings cannot be enabled
                        break;

                    default:

                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ENABLE_LOCATION_HARDWARE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.d(TAG, "onActivityResult: Location Hardware now enabled, checking for permissions");
                    //start location updates
                    checkLocationPermission();
                    break;
                case Activity.RESULT_CANCELED:
                    Log.d(TAG, "onActivityResult: Request to enable location hardware declined. Quitting");
                    break;

                default:
                    Log.d(TAG, "onActivityResult: No Op");
                    break;
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        searchByCityName = false;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        OnSuccessListener<Location> mLocationListener = (Location location) -> {
            if (location != null) {
                Log.d(TAG, "getUserLocation: User Location identified, Getting Weather data for coordinates");
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                mBlizzardViewModel.getWeather(latitude, longitude);
                observeWeatherChanges();
            } else {
                Log.d(TAG, "getUserLocation: Location is null, Requesting periodic Location Updates");
                requestLocationUpdates();
            }
        };

        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), mLocationListener)
                .addOnFailureListener(Throwable::printStackTrace);

    }

    private void observeWeatherChanges() {
        mBlizzardViewModel.getWeatherLiveData().observe(getViewLifecycleOwner(), weatherData -> {
            if (weatherData != null) {
                cityName = weatherData.getName();
                saveToDb(weatherData);
                mTimeUtil.setTime(weatherData.getDt(), weatherData.getTimezone());
                resolveAppState(weatherData);
            } else {
                Snackbar.make(view, "Error getting Location", Snackbar.LENGTH_SHORT).show();
                if (searchByCityName) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(this::reverseViewAnimToInit, 1000L);
                } else {
                    makeViewsInvisible();
                    ivNoInternet.setVisibility(View.VISIBLE);
                    tvNoInternet.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void checkIfIsFavourite() {
        try {
            WeatherDataEntity entity = mBlizzardViewModel.getWeatherByCityName(cityName);
            Log.d(TAG, "checkIfIsFavourite: city name is " + cityName);
            Handler favHandler = new Handler(Looper.getMainLooper());
            favHandler.post(() -> {

                if (searchByCityName) {

                    if (entity.getFavourite()) {
                        isClicked = true;
                        LoadImage(R.drawable.ic_favorite_filed, ivFavourite);
                    } else {
                        isClicked = false;
                        LoadImage(R.drawable.ic_favorite, ivFavourite);
                    }

                    ivFavourite.animate()
                            .alpha(1)
                            .setInterpolator(new AnticipateInterpolator())
                            .setDuration(500)
                            .start();
                    ivFavourite.setVisibility(View.VISIBLE);

                } else {
                    ivFavourite.animate()
                            .alpha(0)
                            .setInterpolator(new AnticipateInterpolator())
                            .setDuration(500)
                            .start();

                    // delayed handler needed for fade out animation to run

                    new Handler(Looper.getMainLooper())
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ivFavourite.setVisibility(View.INVISIBLE);
                                }
                            }, 2000);
                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, "checkIfIsFavourite: Data not saved yet");
        }

    }

    private void saveToDb(WeatherDataResponse weatherDataResponse) {
        Executors.newSingleThreadExecutor().execute(() -> {
            WeatherDataEntity entity = new WeatherMapper(mBlizzardViewModel).mapToEntity(weatherDataResponse);
            mBlizzardViewModel.saveWeather(entity);
            checkIfIsFavourite();
        });
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationUpdatesCallback, Looper.getMainLooper());
        getUserLocation();
    }

    private void stopLocationUpdates() {
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationUpdatesCallback);
            Log.d(TAG, "stopLocationUpdates: Location Updates Stopped");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void resolveAppState(WeatherDataResponse weatherDataResponse) {
        progressBar.setVisibility(View.VISIBLE);
        ivNoInternet.setVisibility(View.INVISIBLE);
        tvNoInternet.setVisibility(View.INVISIBLE);
        String cityName = weatherDataResponse.getName() + ", " + weatherDataResponse.getSys().getCountry();
        tvCityTitle.setText(cityName);

        Double temp = weatherDataResponse.getMain().getTemp();
        tvCityTemp.setText(TempConverter.kelToCelsius(temp));

        String humidity = weatherDataResponse.getMain().getHumidity() + "%";
        tvCityHumidity.setText(humidity);

        Weather weather = weatherDataResponse.getWeather().get(0);
        tvCityDescription.setText(weather.getDescription());

        LoadImage(weather.getIcon());

        String windSpeed = weatherDataResponse.getWind().getSpeed() + " m/s";

        tvCityWindSpeed.setText(windSpeed);

        tvTime.setText(mTimeUtil.getTime());

        progressBar.setVisibility(View.INVISIBLE);

        showViews();
    }

    private void showViews() {
        tvCityTitle.setVisibility(View.VISIBLE);
        tvCityDescription.setVisibility(View.VISIBLE);
        tvCityHumidity.setVisibility(View.VISIBLE);
        tvCityTemp.setVisibility(View.VISIBLE);
        tvCityWindSpeed.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);
        ivWeatherImage.setVisibility(View.VISIBLE);
        tvHumidityTitle.setVisibility(View.VISIBLE);
        tvWindTitle.setVisibility(View.VISIBLE);
    }

    private void LoadImage(String iconId) {
        String url = String.format("http://openweathermap.org/img/wn/%s@4x.png", iconId);

        Glide.with(requireView())
                .load(url)
                .error(R.drawable.ic_cloud)
                .into(ivWeatherImage);
    }

    private void LoadImage(int drawable, ImageView imageView) {
        Glide.with(requireView())
                .load(drawable)
                .into(imageView);
    }
}