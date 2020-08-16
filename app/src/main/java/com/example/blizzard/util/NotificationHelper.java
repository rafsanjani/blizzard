package com.example.blizzard.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.blizzard.R;

public class NotificationHelper {

    private static NotificationHelper instance;
    private final Context context;
    private final static String channelId = "Blizzard_channel";
    private final static int notificationId = 1290;
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


        PendingIntent pendingIntent =
                new NavDeepLinkBuilder(context)
                        .setGraph(R.navigation.nav_graph)
                        .setDestination(R.id.FirstFragment)
                        .createPendingIntent();

        String notificationText = cityName + " has experienced a weather change, with a temperature change from " +
                this.prevTemp + "°C to " + this.curTemp + "°C.";

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setContentTitle(context.getString(R.string.weather_update))
                        .setContentText(notificationText)
                        .setSmallIcon(R.drawable.ic_cloud)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .setBigContentTitle(context.getString(R.string.weather_update))
                                .bigText(notificationText))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            String description = "Notification channel for blizzard";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, channelId, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
    }
}
