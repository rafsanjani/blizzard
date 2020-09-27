package com.example.blizzard.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.blizzard.views.HomeFragment
import com.example.blizzard.R

class NotificationHelper private constructor(val context: Context, private val cityName: String, private val prevTemp: Int, private val curTemp: Int) {

    fun createNotification() {
        createNotificationChannel()
        val bundle = Bundle()
        bundle.putString(HomeFragment.CITY_NAME, cityName)
        val pendingIntent = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.FirstFragment)
                .setArguments(bundle)
                .createPendingIntent()
        val notificationText = "$cityName - from $prevTemp°C to $curTemp°C "
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.weather_update))
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_cloud)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val description = "Notification channel for blizzard"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance)
            channel.description = description
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private var instance: NotificationHelper? = null
        private const val CHANNEL_ID = "Blizzard_channel"
        private const val NOTIFICATION_ID = 1290
        @JvmStatic
        fun getInstance(context: Context, cityName: String, prevTemp: Int, curTemp: Int): NotificationHelper? {
            if (instance == null) {
                instance = NotificationHelper(context, cityName, prevTemp, curTemp)
            }
            return instance
        }
    }

}