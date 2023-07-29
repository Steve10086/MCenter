package com.astune.mcenter;

import android.app.Application;
import androidx.room.util.FileUtil;
import com.astune.mcenter.object.Room.MCenterDB;

import java.io.File;
import java.io.IOException;

public class MCApplication extends Application {
    private static MCApplication MApp;

    public MCApplication getInstance(){return MApp;}

    @Override
    public void onCreate(){
        super.onCreate();
        MApp = this;
        // create database entity
        MCenterDB.Companion.buildDB(this.getApplicationContext());

    }}
