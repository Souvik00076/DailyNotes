package com.example.dailynotes.Adapters;

import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutor {
    private static final Object LOCK = new Object();
    private static AppExecutor instance;
    private final Executor diskIO;
    private final Executor networkIO;

    private AppExecutor(Executor diskIO, Executor networkIO) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
    }

    public static AppExecutor getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new AppExecutor(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3));
            }
        }
        return instance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }
}
