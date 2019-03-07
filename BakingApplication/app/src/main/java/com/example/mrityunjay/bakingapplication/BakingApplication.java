package com.example.mrityunjay.bakingapplication;

import android.app.Application;

import com.example.mrityunjay.bakingapplication.utils.ConnectionReceiver;

public class BakingApplication extends Application {

    private static BakingApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized BakingApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectionReceiver.ConnectivityReceiverListener listener) {
        ConnectionReceiver.connectivityReceiverListener = listener;
    }
}
