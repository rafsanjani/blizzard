package com.example.blizzard.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kelvin_clark on 8/24/2020
 */
public final class BlizzardThread {

    private Executor diskIO;
    private Executor networkIO;
    private Executor mainThreadIO;
    private static BlizzardThread instance;
    private HandlerThread handlerThread;

    BlizzardThread(Executor diskIO, Executor networkIO, Executor mainThreadIO) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThreadIO = mainThreadIO;
        handlerThread = new HandlerThread("blizzard_thread");
    }


    public static BlizzardThread getInstance() {
        if (instance == null) {
            instance = new BlizzardThread(
                    Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(2),
                    new MainThreadIo());
        }

        return instance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThreadIO() {
        return mainThreadIO;
    }

    // for delayed background works
    public Handler getHandler() {
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    private static class MainThreadIo implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());


        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }

    }
}
