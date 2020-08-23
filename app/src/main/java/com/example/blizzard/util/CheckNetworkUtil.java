package com.example.blizzard.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by tony on 8/9/2020
 */

public class CheckNetworkUtil extends BroadcastReceiver {
    public static ConnectivityListener sConnectivityListener;


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = false;
        if (connMgr != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        isNetworkAvailable = true;
                    }
                }
            } else {
                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                    isNetworkAvailable = true;
                }
            }
        }
        return isNetworkAvailable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = false;
        if (connMgr != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        isNetworkAvailable = true;
                    }
                }
            } else {
                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                    isNetworkAvailable= true;
                }
            }
        }
        if (sConnectivityListener != null) {
            sConnectivityListener.onNetworkConnectionChanged(isNetworkAvailable);
        }
    }

    public void setConnectivityListener(CheckNetworkUtil.ConnectivityListener listener) {
        sConnectivityListener = listener;
    }

    //Listener interface
    public interface ConnectivityListener {
        void onNetworkConnectionChanged(boolean isNetworkAvailable);
    }
}
