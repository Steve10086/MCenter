package com.astune.mcenter.ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.object.Room.MCenterDB;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private final MCenterDB db;

    protected final MutableLiveData<String> title = new MutableLiveData<>("main");

    private final CompositeDisposable disposable = new CompositeDisposable();

    protected final MutableLiveData<List<Device>> deviceList = new MutableLiveData<>(new ArrayList<>());

    MainActivityViewModel(){
        Log.i("MainViewModel", "Started");
        db = MCenterDB.Companion.getDB();
    }


    public void refreshDeviceList() {
        disposable.add(Completable.fromAction(() ->{

            assert db != null;
            Log.i("room", "start");
            deviceList.postValue(db.deviceDao().getAll());
            Log.i("room", "finish");

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            Log.i("room", deviceList.getValue().toString());
            Log.i("room", "complete");
            updateOnline();
        }, throwable ->  Log.e("room", throwable.getMessage())
        ));
    }

    public void updateOnline(){
        for (Device device : deviceList.getValue()) {
            disposable.add(Completable.fromAction(() ->{

                if (isOnline(device.getIp())) {//set last online time
                    device.setLastOnline(new Date(System.currentTimeMillis()).toString());
                    db.deviceDao().insert(device);
                    device.setOnLine(true);
                }

            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                        deviceList.getValue().replaceAll((d) -> (d.getId() == device.getId()) ? device : d);
                        deviceList.setValue(deviceList.getValue());//trigger the observer
                    }
            ));
        }
    }

    public void deleteDevice(Device device){
        disposable.add(db.deviceDao().delete(device).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() ->{
            Log.i("room", "delete complete");
            refreshDeviceList();
        }));
    }

    private boolean isOnline(String ip){
        InetAddress address;
        try {
            address = InetAddress.getByName(ip);
            if (address.isReachable(5000)){
                Log.w(address.toString(),  "is online");
                return true;
            }
        }catch (Exception e){
            Log.i("ip", e.getMessage());
        }
        Log.w(ip,  "is offline");
        return false;
    }

    public void insertDevice(Device device, Context context){
        disposable.add(
                MCenterDB.Companion.getDB().deviceDao().insert(device)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(()-> {
                            Toast.makeText(context, "finish", Toast.LENGTH_SHORT).show();
                            refreshDeviceList();
                        }));
    }
}
