package com.astune.mcenter.ui;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.object.Room.MCenterDB;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.core.Single;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class MainActivityViewModel extends ViewModel {
    private final MCenterDB db;
    private LiveData<String> title = new MutableLiveData<>();


    MainActivityViewModel(){
        db = MCenterDB.Companion.getDB();
    }


    @SuppressLint("CheckResult")
    public List<Device> refreshDeviceData() {
        List<Device> deviceList = db.deviceDao().getAll();


        //fills device information basing on room object
        for (Device device : deviceList){
            //set last online time
            if (isOnline(device.getIp())) {
                device.setLastOnline(new Date(System.currentTimeMillis()).toString());
                Single.just(db.deviceDao().insert(device).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())).subscribe();
                device.setOnLine(true);
            }
        }

        return deviceList;
    }

    private boolean isOnline(String ip){
        try {
            InetAddress address = InetAddress.getByName(ip);
            if (address.isReachable(5000)){
                return true;
            }
        }catch (Exception e){
            Log.i("ip", e.getMessage());
        }
        return false;
    }
}
