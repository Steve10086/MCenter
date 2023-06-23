package com.astune.mcenter;

import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;
import androidx.room.Room;
import com.astune.mcenter.object.Device;
import com.astune.mcenter.object.Room.AppDatabase;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private AppDatabase db;


    /*public void initDb(){
        db = Room.databaseBuilder(Context.getApplicationContext(), AppDatabase.class, "device").build();
    }*/


    public List<Device> refreshDeviceData() {
        List<com.astune.mcenter.object.Room.Device> deviceRoomList;
        deviceRoomList = db.deviceDao().getAll();
        List<Device> deviceList = new ArrayList<>();

        //fills device information basing on room object
        for (com.astune.mcenter.object.Room.Device device : deviceRoomList){
            deviceList.add(new Device(device.getName(), device.getIp()));
            //set last online time
            if (isOnline(device.getIp())) {
                deviceList.get(-1).setLastOnline(new Date(System.currentTimeMillis()));
                deviceList.get(-1).changeStatus(true);
            }else{
                deviceList.get(-1).changeStatus(false);
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
