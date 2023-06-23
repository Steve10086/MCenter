package com.astune.mcenter;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.object.Room.MCenterDB;
import com.astune.mcenter.object.Room.databaseBuilder;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MCenterDB db;



    MainActivityViewModel(){
        db = databaseBuilder.getDb();
    }


    public List<Device> refreshDeviceData() {
        List<Device> deviceList = db.deviceDao().getAll();


        //fills device information basing on room object
        for (Device device : deviceList){
            //set last online time
            if (isOnline(device.getIp())) {
                device.setLastOnline(new Date(System.currentTimeMillis()).toString());
                db.deviceDao().update(device);
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
