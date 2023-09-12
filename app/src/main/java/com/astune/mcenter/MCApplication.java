package com.astune.mcenter;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MCApplication extends Application {
    private static MCApplication MApp;

    public MCApplication getInstance(){return MApp;}

    @Override
    public void onCreate(){
        super.onCreate();
        MApp = this;

    }}