package com.example.blizzard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.blizzard.data.database.WeatherMapper
import com.example.blizzard.model.WeatherDataResponse
import com.example.blizzard.util.NetworkMonitor
import com.example.blizzard.util.TempConverter
import com.example.blizzard.util.TimeUtil
import com.example.blizzard.viewmodel.BlizzardViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*
import java.util.*
import java.util.concurrent.Executors

class HomeFragment : Fragment() {
    private val mTimeUtil = TimeUtil()
    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationUpdatesCallback: LocationCallback? = null
    private var mBlizzardViewModel: BlizzardViewModel? = null
    private var curLocIsVisible = true
    private var slideRight: Animation? = null
    private var slideLeft: Animation? = null
    private var searchByCityName = false
    private var isClicked = false
    private var cityName: String? = null
    private var showDialogOnce = 0
    private var mDeviceConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBlizzardViewModel = ViewModelProvider(requireActivity()).get(BlizzardViewModel::class.java)
        slideRight = AnimationUtils.loadAnimation(context, R.anim.slide_right)
        slideLeft = AnimationUtils.loadAnimation(context, R.anim.slide_left)
        mLocationUpdatesCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d(TAG, "onLocationResult: Periodic Location Callback Triggered. Stopping Updates")
                stopLocationUpdates()
                val location = locationResult.lastLocation
                val latitude = location.latitude
                val longitude = location.longitude
                mBlizzardViewModel?.getWeather(latitude, longitude)
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        makeViewsInvisible()
        val bundle = this.arguments
        val networkMonitor = NetworkMonitor(requireContext())
        networkMonitor.observe(viewLifecycleOwner, { aBoolean: Boolean ->
            mDeviceConnected = aBoolean
            showSnackBar(aBoolean)
            if (bundle == null) {
                val appState = mBlizzardViewModel?.appState
                val savedCityName = appState?.get(0)
                val savedTextBoxText = appState?.get(1)
                if (savedCityName != null && savedCityName.isNotEmpty()) {
                    loadByCityName(savedCityName)
                    if (savedTextBoxText != null && savedTextBoxText.isNotEmpty()) {
                        et_cityName.setText(savedTextBoxText)
                    }
                } else {
                    Log.d(TAG, "onViewCreated: no data saved")
                    ensureLocationIsEnabled()
                }
            } else {
                loadByCityName(bundle.getString(CITY_NAME))
            }
        })
        search_btn?.setOnClickListener {
            //Hide the Keyboard when search button is clicked
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            if (Objects.requireNonNull(et_cityName?.text).toString().isEmpty()) {
                et_cityName?.error = "Enter city name"
            } /* else if (!mDeviceConnected) {
                searchBox.setError(getString(R.string.no_internet));
            }*/ else {
                val searchText = et_cityName?.text.toString()
                loadByCityName(searchText)
                animateViews()
            }
        }
        btn_current_location?.setOnClickListener {
            ensureLocationIsEnabled()
            reverseViewAnim()
        }
        fab_search?.setOnClickListener { reverseViewAnimToInit() }
        iv_favourite?.setOnClickListener {
            if (isClicked) {
                isClicked = false
                loadImage(R.drawable.ic_favorite, iv_favourite)
                updateIsFavourite(false)
            } else {
                isClicked = true
                loadImage(R.drawable.ic_favorite_filed, iv_favourite)
                updateIsFavourite(true)
            }
        }
    }

    private fun loadByCityName(city_name: String?) {
        searchByCityName = true
        mBlizzardViewModel?.getWeather(city_name)
        observeWeatherChanges()
    }

    private fun saveState() {
        val searchText = Objects.requireNonNull(et_cityName?.text).toString()
        if (searchText.isNotEmpty()) {
            Log.d(TAG, "saveState: saving state")
            mBlizzardViewModel?.saveAppState(cityName, searchText)
        } else {
            Log.d(TAG, "saveState: saving state")
            mBlizzardViewModel?.saveAppState(cityName)
        }
    }

    private fun updateIsFavourite(b: Boolean) {
        if (cityName != null) {
            Executors.newSingleThreadExecutor()
                    .execute {
                        val weatherDataEntity = mBlizzardViewModel?.getWeatherByCityName(cityName)
                        weatherDataEntity?.favourite = b
                        mBlizzardViewModel?.updateWeatherData(weatherDataEntity)
                    }
        }
    }

    private fun reverseViewAnimToInit() {
        if (!curLocIsVisible) {
            fab_search?.startAnimation(slideRight)
            etLayoutContainer?.startAnimation(slideLeft)
            search_btn?.startAnimation(slideLeft)
            fab_search?.visibility = View.INVISIBLE
            fab_search?.isClickable = false
            etLayoutContainer?.visibility = View.VISIBLE
            search_btn?.visibility = View.VISIBLE
            search_btn?.isClickable = true
            curLocIsVisible = true
        } else {
            fab_search?.startAnimation(slideRight)
            btn_current_location?.startAnimation(slideRight)
            etLayoutContainer?.startAnimation(slideLeft)
            search_btn?.startAnimation(slideLeft)
            fab_search?.visibility = View.INVISIBLE
            fab_search?.isClickable = false
            btn_current_location?.visibility = View.INVISIBLE
            btn_current_location?.isClickable = false
            etLayoutContainer?.visibility = View.VISIBLE
            search_btn?.visibility = View.VISIBLE
            search_btn?.isClickable = true
            curLocIsVisible = true
        }
    }

    private fun reverseViewAnim() {
        curLocIsVisible = false
        btn_current_location?.startAnimation(slideRight)
        btn_current_location?.visibility = View.INVISIBLE
        btn_current_location?.isClickable = false
    }

    private fun animateViews() {
        etLayoutContainer?.startAnimation(slideRight)
        search_btn?.startAnimation(slideRight)
        btn_current_location?.startAnimation(slideLeft)
        fab_search?.startAnimation(slideLeft)
        etLayoutContainer?.visibility = View.INVISIBLE
        search_btn?.visibility = View.INVISIBLE
        search_btn?.isClickable = false
        btn_current_location?.visibility = View.VISIBLE
        btn_current_location?.isClickable = true
        fab_search?.visibility = View.VISIBLE
        fab_search?.isClickable = true
    }


    private fun makeViewsInvisible() {
        tv_cityName?.visibility = View.INVISIBLE
        tv_weatherDescription?.visibility = View.INVISIBLE
        tv_humidityValue?.visibility = View.INVISIBLE
        tv_tempValue?.visibility = View.INVISIBLE
        tv_windSpeed?.visibility = View.INVISIBLE
        tv_dayTime?.visibility = View.INVISIBLE
        weather_icon?.visibility = View.INVISIBLE
        //        progressBar.setVisibility(View.INVISIBLE);
        tv_humidityTitle?.visibility = View.INVISIBLE
        tv_windTitle?.visibility = View.INVISIBLE
    }

    private fun makeProgressBarInvisible() {
        data_loading?.visibility = View.INVISIBLE
        iv_favourite?.visibility = View.INVISIBLE
    }

    private fun checkLocationPermission() {
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
                Log.d(TAG, "checkLocationPermission: " + "Requesting Location Permission Normally")
                requestLocationPermission()
            }
        } else {
            //location permission has been granted, we can proceed and obtain the user's location
            Log.d(TAG, "checkLocationPermission: Permission Granted: Getting User Location")
            userLocation
        }
    }

    private fun requestLocationPermission() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        requestPermissions(permissions, LOCATION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                Log.d(TAG, "onRequestPermissionsResult: Location Permission Granted, Requesting User Location")
                userLocation
            } else {
                Log.d(TAG, "onRequestPermissionsResult: Location Permission Denied, Quitting")
                //permission rejected
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun ensureLocationIsEnabled() {
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
                Log.d(TAG, "ensureLocationIsEnabled: Location Hardware previously enabled: Checking for permissions")

                //ask for location request
                checkLocationPermission()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.d(TAG, "ensureLocationIsEnabled: Location Hardware disabled: Asking user to enable")
                        val resolvable = exception as ResolvableApiException
                        try {
                            startIntentSenderForResult(resolvable.resolution.intentSender, ENABLE_LOCATION_HARDWARE,
                                    null, 0, 0, 0, null)
                        } catch (e: SendIntentException) {
                            e.printStackTrace()
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.d(TAG, "ensureLocationIsEnabled: Location Hardware disabled: Unable to toggle it on. Quitting")
                    else -> {
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ENABLE_LOCATION_HARDWARE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(TAG, "onActivityResult: Location Hardware now enabled, checking for permissions")
                    //start location updates
                    checkLocationPermission()
                }
                Activity.RESULT_CANCELED -> Log.d(TAG, "onActivityResult: Request to enable location hardware declined. Quitting")
                else -> Log.d(TAG, "onActivityResult: No Op")
            }
        }
    }

    @get:SuppressLint("MissingPermission")
    private val userLocation: Unit
        get() {
            searchByCityName = false
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            val mLocationListener = OnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d(TAG, "getUserLocation: User Location identified, Getting Weather data for coordinates")
                    val latitude = location.latitude
                    val longitude = location.longitude
                    mBlizzardViewModel?.getWeather(latitude, longitude)
                    observeWeatherChanges()
                } else {
                    Log.d(TAG, "getUserLocation: Location is null, Requesting periodic Location Updates")
                    requestLocationUpdates()
                }
            }
            mFusedLocationProviderClient?.lastLocation
                    ?.addOnSuccessListener(requireActivity(), mLocationListener)
                    ?.addOnFailureListener { obj: Exception -> obj.printStackTrace() }
        }

    private fun observeWeatherChanges() {
        mBlizzardViewModel?.weatherLiveData?.observe(viewLifecycleOwner, { weatherData: WeatherDataResponse? ->
            if (weatherData != null) {
                cityName = weatherData.name
                saveState()
                saveToDb(weatherData)
                mTimeUtil.setTime(weatherData.dt, weatherData.timezone)
                resolveAppState(weatherData)
                showDialogOnce++
            } else {
                if (searchByCityName) {
                    if (!mDeviceConnected) {
                        showSnackBar(false)
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed({ reverseViewAnimToInit() }, 1000L)
                    } else {
                        Snackbar.make(requireView(), "Error getting Location", Snackbar.LENGTH_SHORT).show()
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed({ reverseViewAnimToInit() }, 1000L)
                    }
                } else if (!mDeviceConnected) {
                    if (showDialogOnce < 1) showNetworkDialog()
                    showDialogOnce++
                    makeProgressBarInvisible()
                    if (!searchByCityName) makeViewsInvisible()
                    iv_no_internet?.visibility = View.VISIBLE
                    tv_no_internet?.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun checkIfIsFavourite() {
        try {
            val entity = mBlizzardViewModel?.getWeatherByCityName(cityName)
            Log.d(TAG, "checkIfIsFavourite: city name is $cityName")
            val favHandler = Handler(Looper.getMainLooper())
            favHandler.post {
                if (searchByCityName) {
                    if (entity?.favourite == true) {
                        isClicked = true
                        loadImage(R.drawable.ic_favorite_filed, iv_favourite)
                    } else {
                        isClicked = false
                        loadImage(R.drawable.ic_favorite, iv_favourite)
                    }
                    iv_favourite?.animate()
                            ?.alpha(1f)
                            ?.setInterpolator(AnticipateInterpolator())
                            ?.setDuration(100)
                            ?.start()
                    iv_favourite?.visibility = View.VISIBLE
                } else {
                    iv_favourite?.animate()
                            ?.alpha(0f)
                            ?.setInterpolator(AnticipateInterpolator())
                            ?.setDuration(100)
                            ?.start()

                    // delayed handler needed for fade out animation to run
                    Handler(Looper.getMainLooper())
                            .postDelayed({ iv_favourite?.visibility = View.INVISIBLE }, 2000)
                }
            }
        } catch (e: NullPointerException) {
            Log.e(TAG, "checkIfIsFavourite: Data not saved yet")
        }
    }

    private fun saveToDb(weatherDataResponse: WeatherDataResponse) {
        Executors.newSingleThreadExecutor().execute {
            val entity = WeatherMapper(mBlizzardViewModel).mapToEntity(weatherDataResponse)
            mBlizzardViewModel?.saveWeather(entity)
            checkIfIsFavourite()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        mFusedLocationProviderClient?.requestLocationUpdates(mLocationRequest, mLocationUpdatesCallback, Looper.getMainLooper())
        userLocation
    }

    private fun stopLocationUpdates() {
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient?.removeLocationUpdates(mLocationUpdatesCallback)
            Log.d(TAG, "stopLocationUpdates: Location Updates Stopped")
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun resolveAppState(weatherDataResponse: WeatherDataResponse) {
        data_loading?.visibility = View.VISIBLE
        iv_no_internet?.visibility = View.INVISIBLE
        tv_no_internet?.visibility = View.INVISIBLE
        val cityName = weatherDataResponse.name + ", " + weatherDataResponse.sys.country
        tv_cityName?.text = cityName
        val temp = weatherDataResponse.main.temp
        tv_tempValue?.text = TempConverter.kelToCelsius(temp)
        val humidity = weatherDataResponse.main.humidity.toString() + "%"
        tv_humidityValue?.text = humidity
        val weather = weatherDataResponse.weather[0]
        tv_weatherDescription?.text = weather.description
        loadImage(weather.icon)
        val windSpeed = weatherDataResponse.wind.speed.toString() + " m/s"
        tv_windSpeed?.text = windSpeed
        tv_dayTime?.text = mTimeUtil.time
        data_loading?.visibility = View.INVISIBLE
        showViews()
    }

    private fun showViews() {
        tv_cityName?.visibility = View.VISIBLE
        tv_weatherDescription?.visibility = View.VISIBLE
        tv_humidityValue?.visibility = View.VISIBLE
        tv_tempValue?.visibility = View.VISIBLE
        tv_windSpeed?.visibility = View.VISIBLE
        tv_dayTime?.visibility = View.VISIBLE
        weather_icon?.visibility = View.VISIBLE
        tv_humidityTitle?.visibility = View.VISIBLE
        tv_windTitle?.visibility = View.VISIBLE
    }

    private fun loadImage(iconId: String) {
        val url = String.format("http://openweathermap.org/img/wn/%s@4x.png", iconId)
        weather_icon?.let {
            Glide.with(requireView())
                    .load(url)
                    .error(R.drawable.ic_cloud)
                    .into(it)
        }
    }

    private fun loadImage(drawable: Int, imageView: ImageView?) {
        if (imageView != null) {
            Glide.with(requireView())
                    .load(drawable)
                    .into(imageView)
        }
    }

    private fun showNetworkDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext(),
                R.style.RoundShapeTheme)
        val customTitleView = View.inflate(requireContext(), R.layout.alert_dialog, null)
        materialAlertDialogBuilder
                .setCustomTitle(customTitleView)
                .setMessage("""    No internet connection found!
                            Please, turn on your Mobile data and hit OK""")
                .setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                    if (mDeviceConnected) {
                        ensureLocationIsEnabled()
                    }
                }
                .setNeutralButton("LATER") { _: DialogInterface?, _: Int -> }
                .setCancelable(false)
                .show()
    }

    private fun showSnackBar(isNetworkAvailable: Boolean) {
        val message: String
        val color: Int
        if (isNetworkAvailable) {
            message = "You are Online"
            color = Color.WHITE
            val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(resources.getColor(R.color.snackBarOnline, null))
            val view = snackbar.view
            val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(color)
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            snackbar.show()
        } else {
            message = "You are Offline"
            color = Color.WHITE
            val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
            val view = snackbar.view
            val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(color)
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            snackbar.show()
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
        const val ENABLE_LOCATION_HARDWARE = 3030
        private const val LOCATION_REQUEST_CODE = 123
        const val CITY_NAME = "com.example.blizzard.cityName"
    }
}