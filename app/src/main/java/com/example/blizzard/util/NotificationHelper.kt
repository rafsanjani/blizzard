package com.example.blizzard.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.blizzard.HomeFragment;
import com.example.blizzard.R;

public class NotificationHelper {

    private static NotificationHelper instance;
    private final Context context;
    private final static String CHANNEL_ID = "Blizzard_channel";
    private final static int NOTIFICATION_ID = 1290;
    private final String cityName;
    private final int prevTemp;
    private final int curTemp;


    private NotificationHelper(Context context, String cityName, int prevTemp, int curTemp) {
        this.context = context.getApplicationContext();
        this.cityName = cityName;
        this.prevTemp = prevTemp;
        this.curTemp = curTemp;
    }

    public static NotificationHelper getInstance(Context context, String cityName, int prevTemp, int curTemp) {
        if (instance == null) {
            instance = new NotificationHelper(context, cityName, prevTemp, curTemp);
        }
        return instance;
    }

    public void createNotification() {

        createNotificationChannel();

        Bundle bundle = new Bundle();
        bundle.putString(HomeFragment.CITY_NAME, cityName);

        PendingIntent pendingIntent =
                new NavDeepLinkBuilder(context)
                        .setGraph(R.navigation.nav_graph)
                        .setDestination(R.id.FirstFragment)
                        .setArguments(bundle)
                        .createPendingIntent();

        String notificationText = cityName + " - from " + prevTemp + "°C to " + curTemp + "°C ";

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(context.getString(R.string.weather_update))
                        .setContentText(notificationText)
                        .setSmallIcon(R.drawable.ic_cloud)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            String description = "Notification channel for blizzard";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
    }
}
