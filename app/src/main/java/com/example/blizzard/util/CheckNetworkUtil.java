package com.example.blizzard.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by tony on 8/9/2020
 */

public class CheckNetworkUtil {
    public Context mContext;

    public CheckNetworkUtil(Context context) {
        this.mContext = context;
    }

    public boolean isNetworkAvailable() {
        boolean isNetworkAvailable = false;
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    isNetworkAvailable = true;
                }
            }
        }
        return isNetworkAvailable;
    }
}
