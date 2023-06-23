package com.astune.mcenter.object.Room;

import android.content.Context;
import androidx.room.Room;

public class databaseBuilder {
    private static MCenterDB db;
    public static void init(Context appContext){
        new Thread(() -> {
            db = Room.databaseBuilder(appContext, MCenterDB.class, "MCenterDB").build();
        }).start();
    }

    public static MCenterDB getDb() {
        try {
            assert (db != null);
        }catch (Exception e){
            return null;
        }
        return db;
    }
}
