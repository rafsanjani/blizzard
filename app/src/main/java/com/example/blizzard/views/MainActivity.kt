package com.example.blizzard.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.work.*
import com.example.blizzard.R
import com.example.blizzard.databinding.ActivityMainBinding
import com.example.blizzard.workers.DataUpdateWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
        startWorker()
    }

    private fun startWorker() {
        Log.d(TAG, "startWorker: Worker Manager initializing work")
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val request = PeriodicWorkRequest.Builder(DataUpdateWorker::class.java, 1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(15, TimeUnit.SECONDS)
                .build()
        WorkManager.getInstance(applicationContext)
                .enqueueUniquePeriodicWork(WEATHER_UPDATE_CHECKER, ExistingPeriodicWorkPolicy.KEEP, request)
    }


    companion object {
        private const val TAG = "MainActivity"
        const val WEATHER_UPDATE_CHECKER = "Weather_Update_Checker"
    }
}