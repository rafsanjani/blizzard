package com.example.blizzard.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created by tony on 8/9/2020
 */

public class NetworkMonitor extends LiveData<Boolean> {
    private ConnectivityManager mConnectivityManager;
    private ConnectivityManager.NetworkCallback mNetworkCallback;

    public NetworkMonitor(Context context) {
        mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkCallback = new NetworkCallback(this);
    }

    @Override
    protected void onActive() {
        super.onActive();
        networkUpdate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mConnectivityManager.registerDefaultNetworkCallback(mNetworkCallback);
        } else {
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build();
            mConnectivityManager.registerNetworkCallback(networkRequest, mNetworkCallback);
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        //unregister to avoid memory leak
        mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
    }

    public static class NetworkCallback extends ConnectivityManager.NetworkCallback {

        private NetworkMonitor mNetworkMonitor;

        public NetworkCallback(NetworkMonitor networkMonitor) {
            mNetworkMonitor = networkMonitor;
        }

        @Override
        public void onAvailable(@NotNull Network network) {
            mNetworkMonitor.postValue(true);
        }

        @Override
        public void onLost(@NotNull Network network) {
            mNetworkMonitor.postValue(false);
        }
    }

    private void networkUpdate() {
        if (mConnectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = mConnectivityManager
                        .getNetworkCapabilities(mConnectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        postValue(true);
                    } else {
                        postValue(false);
                    }
                }
            } else {
                NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                    postValue(true);
                } else {
                    postValue(false);
                }
            }
        }
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
                networkUpdate();
            }
        }
    }
}
