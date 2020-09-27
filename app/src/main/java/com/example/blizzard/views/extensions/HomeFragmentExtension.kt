package com.example.blizzard.views.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.blizzard.R
import com.example.blizzard.data.database.WeatherMapper
import com.example.blizzard.model.WeatherDataResponse
import com.example.blizzard.model.extensions.StringCelsius
import com.example.blizzard.views.HomeFragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
Create by kelvin clark on 9/27/2020
 */

fun HomeFragment.animateViews() {
    etLayoutContainer.startAnimation(slideRight)
    search_btn.startAnimation(slideRight)
    btn_current_location.startAnimation(slideLeft)
    fab_search.startAnimation(slideLeft)
    etLayoutContainer.visibility = View.INVISIBLE
    search_btn.visibility = View.INVISIBLE
    search_btn.isClickable = false
    btn_current_location.visibility = View.VISIBLE
    btn_current_location.isClickable = true
    fab_search.visibility = View.VISIBLE
    fab_search.isClickable = true
}

fun HomeFragment.showViews() {
    tv_cityName?.visibility = View.VISIBLE
    tv_weatherDescription!!.visibility = View.VISIBLE
    tv_humidityValue?.visibility = View.VISIBLE
    tv_tempValue?.visibility = View.VISIBLE
    tv_windSpeed?.visibility = View.VISIBLE
    tv_dayTime?.visibility = View.VISIBLE
    weather_icon?.visibility = View.VISIBLE
    tv_humidityTitle.visibility = View.VISIBLE
    tv_windTitle.visibility = View.VISIBLE
}

fun HomeFragment.reverseViewAnimToInit() {
    if (!curLocIsVisible) {
        fab_search.startAnimation(slideRight)
        etLayoutContainer.startAnimation(slideLeft)
        search_btn.startAnimation(slideLeft)
        fab_search.visibility = View.INVISIBLE
        fab_search.isClickable = false
        etLayoutContainer.visibility = View.VISIBLE
        search_btn.visibility = View.VISIBLE
        search_btn.isClickable = true
        curLocIsVisible = true
    } else {
        fab_search.startAnimation(slideRight)
        btn_current_location.startAnimation(slideRight)
        etLayoutContainer.startAnimation(slideLeft)
        search_btn.startAnimation(slideLeft)
        fab_search.visibility = View.INVISIBLE
        fab_search.isClickable = false
        etLayoutContainer.visibility = View.INVISIBLE
        etLayoutContainer.isClickable = false
        etLayoutContainer.visibility = View.VISIBLE
        search_btn.visibility = View.VISIBLE
        search_btn.isClickable = true
        curLocIsVisible = true
        btn_current_location.visibility = View.INVISIBLE
        btn_current_location.isClickable = false
    }
}

fun HomeFragment.reverseViewAnim() {
    curLocIsVisible = false
    btn_current_location.startAnimation(slideRight)
    btn_current_location.visibility = View.INVISIBLE
    btn_current_location.isClickable = false
}

fun HomeFragment.makeViewsInvisible() {
    tv_cityName.visibility = View.INVISIBLE
    tv_weatherDescription.visibility = View.INVISIBLE
    tv_humidityValue.visibility = View.INVISIBLE
    tv_tempValue.visibility = View.INVISIBLE
    tv_windSpeed.visibility = View.INVISIBLE
    tv_dayTime.visibility = View.INVISIBLE
    weather_icon.visibility = View.INVISIBLE
    tv_humidityTitle.visibility = View.INVISIBLE
    tv_windTitle.visibility = View.INVISIBLE
}

fun HomeFragment.makeProgressBarInvisible() {
    data_loading.visibility = View.INVISIBLE
    iv_favourite.visibility = View.INVISIBLE
}

fun HomeFragment.showProgressBar() {
    data_loading.visibility = View.VISIBLE
    makeViewsInvisible()
    iv_favourite.visibility = View.INVISIBLE
    iv_no_internet.visibility = View.INVISIBLE
    tv_no_internet.visibility = View.INVISIBLE
}

fun HomeFragment.showSnackBar() {
    val message = "You are Offline"
    val color: Int = Color.WHITE
    val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
    val view = snackbar.view
    val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.setTextColor(color)
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snackbar.show()
}

fun HomeFragment.resolveAppState(weatherDataResponse: WeatherDataResponse) {
    data_loading.visibility = View.VISIBLE
    iv_no_internet.visibility = View.INVISIBLE
    tv_no_internet.visibility = View.INVISIBLE
    val cityName = weatherDataResponse.name + ", " + weatherDataResponse.sys?.country
    tv_cityName?.text = cityName
    tv_tempValue?.text = weatherDataResponse.StringCelsius
    val humidity = weatherDataResponse.main?.humidity.toString() + "%"
    tv_humidityValue?.text = humidity
    val weather = weatherDataResponse.weather?.get(0)
    tv_weatherDescription?.text = weather?.description
    weather?.icon?.let { loadImage(it) }
    val windSpeed = weatherDataResponse.wind?.speed.toString() + " m/s"
    tv_windSpeed?.text = windSpeed
    tv_dayTime?.text = mTimeUtil.timeAmPm
    data_loading.visibility = View.INVISIBLE
    showViews()
}

fun HomeFragment.saveState() {
    val searchText = Objects.requireNonNull(et_cityName.text).toString()
    if (searchText.isNotEmpty()) {
        Log.d(HomeFragment.TAG, "saveState: saving state")
        mBlizzardViewModel?.saveAppState(cityName, searchText)
    } else {
        Log.d(HomeFragment.TAG, "saveState: saving state")
        searchByCityName = false
        mBlizzardViewModel?.saveAppState(cityName)
    }
}

suspend fun HomeFragment.checkIfIsFavourite() {
    try {
        val entity = mBlizzardViewModel?.getWeatherByCityName(cityName)
        Log.d(HomeFragment.TAG, "checkIfIsFavourite: city name is $cityName")
        withContext(Dispatchers.Main) {
            if (searchByCityName) {
                if (entity != null) {
                    if (entity.favourite!!) {
                        isClicked = true
                        loadImage(R.drawable.ic_favorite_filed, iv_favourite)
                    } else {
                        isClicked = false
                        loadImage(R.drawable.ic_favorite, iv_favourite)
                    }
                }
                iv_favourite.animate()
                        .alpha(1f)
                        .setInterpolator(AnticipateInterpolator())
                        .setDuration(100)
                        .start()
                iv_favourite.visibility = View.VISIBLE
            } else {
                iv_favourite.animate()
                        .alpha(0f)
                        .setInterpolator(AnticipateInterpolator())
                        .setDuration(100)
                        .start()

                // delayed handler needed for fade out animation to run
                delay(2000L)
                iv_favourite.visibility = View.INVISIBLE
            }
        }
    } catch (e: NullPointerException) {
        Log.e(HomeFragment.TAG, "checkIfIsFavourite: Data not saved yet")
    }
}

fun HomeFragment.loadImage(iconId: String) {
    val url = String.format("http://openweathermap.org/img/wn/%s@4x.png", iconId)
    weather_icon?.let {
        Glide.with(requireView())
                .load(url)
                .error(R.drawable.ic_cloud)
                .into(it)
    }
}

fun HomeFragment.loadImage(drawable: Int, imageView: ImageView?) {
    Glide.with(requireView())
            .load(drawable)
            .into(imageView!!)
}

suspend fun HomeFragment.updateIsFavourite(b: Boolean) {
    if (cityName != null) {
        val weatherDataEntity = mBlizzardViewModel?.getWeatherByCityName(cityName)
        weatherDataEntity?.favourite = b
        mBlizzardViewModel?.updateWeatherData(weatherDataEntity)
    }
}

fun HomeFragment.saveToDb(weatherDataResponse: WeatherDataResponse) {
    lifecycleScope.launch(Dispatchers.IO) {
        val entity = mBlizzardViewModel?.let { WeatherMapper(it).mapToEntity(weatherDataResponse) }
        mBlizzardViewModel?.saveWeather(entity)
        checkIfIsFavourite()
    }
}

fun HomeFragment.showNetworkDialog() {
    val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext(),
            R.style.RoundShapeTheme)
    val customTitleView = View.inflate(requireContext(), R.layout.alert_dialog, null)
    materialAlertDialogBuilder
            .setCustomTitle(customTitleView)
            .setMessage("""    No internet connection found!
                            Please, turn on your Mobile data and hit OK""")
            .setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                if (HomeFragment.mDeviceConnected) {
                    ensureLocationIsEnabled()
                }
            }
            .setNeutralButton("LATER") { _: DialogInterface?, _: Int -> }
            .setCancelable(false)
            .show()
}

fun HomeFragment.observeWeatherChanges(liveData: LiveData<WeatherDataResponse?>?) {
    liveData?.observe(viewLifecycleOwner, { weatherData: WeatherDataResponse? ->
        if (weatherData != null) {
            HomeFragment.mDeviceConnected = true
            if (searchByCityName)
                cityName = weatherData.name
            saveToDb(weatherData)
            mTimeUtil.setTime(weatherData.dt, weatherData.timezone)
            resolveAppState(weatherData)
            showDialogOnce++
        } else {
            if (searchByCityName) {
                if (!HomeFragment.mDeviceConnected) {
                    showSnackBar()
                    lifecycleScope.launch {
                        delay(1000L)
                        reverseViewAnimToInit()
                    }
                } else {
                    Snackbar.make(requireView(), "Error getting Location", Snackbar.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        delay(1000L)
                        reverseViewAnimToInit()
                    }
                }
            } else if (!HomeFragment.mDeviceConnected) {
                if (showDialogOnce < 1) showNetworkDialog()
                showDialogOnce++
                makeProgressBarInvisible()
                if (!searchByCityName) makeViewsInvisible()
                iv_no_internet.visibility = View.VISIBLE
                tv_no_internet.visibility = View.VISIBLE
            }
        }
    })
}

fun HomeFragment.ensureLocationIsEnabled() {
    mLocationRequest = LocationRequest.create()
    mLocationRequest?.interval = 10000
    mLocationRequest?.fastestInterval = 5000
    mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    val builder = LocationSettingsRequest.Builder()
    builder.addLocationRequest(mLocationRequest!!)
    val settingsClient = LocationServices.getSettingsClient(requireActivity())
    val locationResponse = settingsClient.checkLocationSettings(builder.build())
    locationResponse.addOnCompleteListener { task: Task<LocationSettingsResponse?> ->
        try {
            task.getResult(ApiException::class.java)
            Log.d(HomeFragment.TAG, "ensureLocationIsEnabled: Location Hardware previously enabled: Checking for permissions")

            //ask for location request
            checkLocationPermission()
        } catch (exception: ApiException) {
            when (exception.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.d(HomeFragment.TAG, "ensureLocationIsEnabled: Location Hardware disabled: Asking user to enable")
                    val resolvable = exception as ResolvableApiException
                    try {
                        startIntentSenderForResult(resolvable.resolution.intentSender, HomeFragment.ENABLE_LOCATION_HARDWARE,
                                null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.d(HomeFragment.TAG, "ensureLocationIsEnabled: Location Hardware disabled: Unable to toggle it on. Quitting")
                else -> {
                }
            }
        }
    }
}

fun HomeFragment.checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder(requireContext())
                    .setTitle(R.string.permissionRationalTitle)
                    .setMessage(R.string.permissionRationalMessage)
                    .setNegativeButton("No") { _: DialogInterface?, _: Int -> ensureLocationIsEnabled() }
                    .setPositiveButton("Ok, ask again") { _: DialogInterface?, _: Int -> requestLocationPermission() }
                    .show()
        } else {
            Log.d(HomeFragment.TAG, "checkLocationPermission: " + "Requesting Location Permission Normally")
            requestLocationPermission()
        }
    } else {
        //location permission has been granted, we can proceed and obtain the user's location
        Log.d(HomeFragment.TAG, "checkLocationPermission: Permission Granted: Getting User Location")
        userLocation
    }
}

@SuppressLint("MissingPermission")
fun HomeFragment.requestLocationUpdates() {
    mFusedLocationProviderClient?.requestLocationUpdates(mLocationRequest, mLocationUpdatesCallback, Looper.getMainLooper())
    userLocation
}

fun HomeFragment.stopLocationUpdates() {
    if (mFusedLocationProviderClient != null) {
        mFusedLocationProviderClient?.removeLocationUpdates(mLocationUpdatesCallback)
        Log.d(HomeFragment.TAG, "stopLocationUpdates: Location Updates Stopped")
    }
}

fun HomeFragment.loadByCityName(city_name: String) {
    lifecycleScope.launch {
        observeWeatherChanges(mBlizzardViewModel?.getWeather(city_name))
    }
}

fun HomeFragment.requestLocationPermission() {
    val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    requestPermissions(permissions, HomeFragment.LOCATION_REQUEST_CODE)
}