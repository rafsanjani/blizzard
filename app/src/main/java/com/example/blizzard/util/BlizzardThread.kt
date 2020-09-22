package com.example.blizzard.util

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by kelvin_clark on 8/24/2020
 */
class BlizzardThread internal constructor(val diskIO: Executor, val networkIO: Executor, val mainThreadIO: Executor) {
    private val handlerThread: HandlerThread = HandlerThread("blizzard_thread")

    // for delayed background works
    val handler: Handler
        get() {
            handlerThread.start()
            return Handler(handlerThread.looper)
        }

    private class MainThreadIo : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(runnable: Runnable) {
            mainThreadHandler.post(runnable)
        }
    }

    companion object {
        @JvmStatic
        var instance: BlizzardThread? = null
            get() {
                if (field == null) {
                    field = BlizzardThread(
                            Executors.newSingleThreadExecutor(),
                            Executors.newFixedThreadPool(2),
                            MainThreadIo())
                }
                return field
            }
            private set
    }

}