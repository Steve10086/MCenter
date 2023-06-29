package com.astune.mcenter;

import android.app.Application;
import com.astune.mcenter.object.Room.MCenterDB;

public class MCApplication extends Application {
    private static MCApplication MApp;

    public MCApplication getInstance(){return MApp;}

    @Override
    public void onCreate(){
        super.onCreate();
        MApp = this;

        MCenterDB.Companion.buildDB(this.getApplicationContext());

    }}
