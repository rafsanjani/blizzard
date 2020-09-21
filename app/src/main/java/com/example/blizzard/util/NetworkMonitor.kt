package com.example.blizzard.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData

/**
 * Created by tony on 8/9/2020
 */
class NetworkMonitor(context: Context) : LiveData<Boolean?>() {
    private val mConnectivityManager: ConnectivityManager?
    private val mNetworkCallback: ConnectivityManager.NetworkCallback
    override fun onActive() {
        super.onActive()
        networkUpdate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mConnectivityManager!!.registerDefaultNetworkCallback(mNetworkCallback)
        } else {
            val networkRequest = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build()
            mConnectivityManager!!.registerNetworkCallback(networkRequest, mNetworkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        //unregister to avoid memory leak
        mConnectivityManager!!.unregisterNetworkCallback(mNetworkCallback)
    }

    class NetworkCallback(private val mNetworkMonitor: NetworkMonitor) : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            mNetworkMonitor.postValue(true)
        }

        override fun onLost(network: Network) {
            mNetworkMonitor.postValue(false)
        }
    }

    private fun networkUpdate() {
        if (mConnectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = mConnectivityManager
                        .getNetworkCapabilities(mConnectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        postValue(true)
                    } else {
                        postValue(false)
                    }
                }
            } else {
                val activeNetworkInfo = mConnectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting) {
                    postValue(true)
                } else {
                    postValue(false)
                }
            }
        }
    }

    inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                networkUpdate()
            }
        }
    }

    init {
        mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        mNetworkCallback = NetworkCallback(this)
    }
}