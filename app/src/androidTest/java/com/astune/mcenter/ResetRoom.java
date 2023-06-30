package com.astune.mcenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.object.Room.MCenterDB;
import io.reactivex.Single;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ResetRoom {
    @SuppressLint("CheckResult")
    @Test
    public void reset(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MCenterDB.Companion.buildDB(appContext);

        MCenterDB.Companion.getDB().deviceDao().insert(new Device(0, "test", "1234", null)).subscribe(() ->{
            Log.i("room", MCenterDB.Companion.getDB().deviceDao().getAll().toString());
            MCenterDB.Companion.getDB().deviceDao().deleteAll();
            MCenterDB.Companion.getDB().deviceDao().resetPrimaryKey();
            Log.i("roomfinish", MCenterDB.Companion.getDB().deviceDao().getAll().toString());
        }, Throwable::printStackTrace);
    }
}
