package com.example.blizzard.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.blizzard.R
import com.example.blizzard.util.TimeUtil
import com.example.blizzard.viewmodel.BlizzardViewModel
import com.example.blizzard.views.extensions.*
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    val mTimeUtil = TimeUtil()
    var mLocationRequest: LocationRequest? = null
    var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    var mLocationUpdatesCallback: LocationCallback? = null
    var mBlizzardViewModel: BlizzardViewModel? = null
    var curLocIsVisible = true
    var slideRight: Animation? = null
    var slideLeft: Animation? = null
    var searchByCityName = false
    var isClicked = false
    var cityName: String? = null
    var showDialogOnce = 0
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
                lifecycleScope.launch {
                    mBlizzardViewModel?.getWeather(latitude, longitude)
                }
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onStop() {
        super.onStop()
        saveState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        makeViewsInvisible()
        val bundle = this.arguments
        if (bundle == null) {
            val appState = mBlizzardViewModel!!.appState
            val savedCityName = appState[0]
            val savedSearchBoxText = appState[1]
            if (savedSearchBoxText.isNullOrEmpty())
                searchByCityName = false
            if (savedCityName != null && savedCityName.isNotEmpty()) {
                loadByCityName(savedCityName)
                if (savedSearchBoxText != null && savedSearchBoxText.isNotEmpty()) {
                    et_cityName.setText(savedSearchBoxText)
                }
            } else {
                Log.d(TAG, "onViewCreated: no data saved")
                ensureLocationIsEnabled()
            }
        } else {
            searchByCityName = true
            bundle.getString(CITY_NAME)?.let { loadByCityName(it) }
        }

        search_btn?.setOnClickListener {
            //Hide the Keyboard when search button is clicked
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            if (et_cityName.text.toString().isEmpty()) {
                et_cityName.error = "Enter city name"
            } else {
                showProgressBar()
                val searchText = et_cityName.text.toString()
                searchByCityName = true
                loadByCityName(searchText)
                animateViews()
            }
        }
        btn_current_location.setOnClickListener {
            et_cityName.setText("")
            searchByCityName = false
            showProgressBar()
            ensureLocationIsEnabled()
            reverseViewAnim()
        }
        fab_search?.setOnClickListener { reverseViewAnimToInit() }
        iv_favourite.setOnClickListener {
            if (isClicked) {
                isClicked = false
                loadImage(R.drawable.ic_favorite, iv_favourite)
                lifecycleScope.launch(IO) {
                    updateIsFavourite(false)
                }
            } else {
                isClicked = true
                loadImage(R.drawable.ic_favorite_filed, iv_favourite)
                lifecycleScope.launch(IO) {
                    updateIsFavourite(true)
                }
            }
        }
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
    val userLocation: Unit
        get() {
            searchByCityName = false
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            val mLocationListener = OnSuccessListener<Location> { location: Location? ->
                if (location != null) {
                    Log.d(TAG, "getUserLocation: User Location identified, Getting Weather data for coordinates")
                    val latitude = location.latitude
                    val longitude = location.longitude
                    lifecycleScope.launch {
                        observeWeatherChanges(mBlizzardViewModel?.getWeather(latitude, longitude))
                    }
                } else {
                    Log.d(TAG, "getUserLocation: Location is null, Requesting periodic Location Updates")
                    requestLocationUpdates()
                }
            }
            mFusedLocationProviderClient?.lastLocation
                    ?.addOnSuccessListener(requireActivity(), mLocationListener)
                    ?.addOnFailureListener { obj: Exception -> obj.printStackTrace() }
        }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    companion object {
        const val TAG = "HomeFragment"
        const val ENABLE_LOCATION_HARDWARE = 3030
        const val LOCATION_REQUEST_CODE = 123
        const val CITY_NAME = "com.example.blizzard.cityName"

        @JvmStatic
        var mDeviceConnected = false
    }
}