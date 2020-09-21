package com.example.blizzard.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Created by tony on 8/9/2020
 */
class CheckNetworkUtil : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isNetworkAvailable = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    isNetworkAvailable = true
                }
            }
        } else {
            val activeNetworkInfo = connMgr.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting) {
                isNetworkAvailable = true
            }
        }
        if (sConnectivityListener != null) {
            sConnectivityListener!!.onNetworkConnectionChanged(isNetworkAvailable)
        }
    }

    fun setConnectivityListener(listener: ConnectivityListener?) {
        sConnectivityListener = listener
    }

    //Listener interface
    interface ConnectivityListener {
        fun onNetworkConnectionChanged(isNetworkAvailable: Boolean)
    }

    companion object {
        var sConnectivityListener: ConnectivityListener? = null
        fun isNetworkAvailable(context: Context): Boolean {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var isNetworkAvailable = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        isNetworkAvailable = true
                    }
                }
            } else {
                val activeNetworkInfo = connMgr.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting) {
                    isNetworkAvailable = true
                }
            }
            return isNetworkAvailable
        }
    }
}