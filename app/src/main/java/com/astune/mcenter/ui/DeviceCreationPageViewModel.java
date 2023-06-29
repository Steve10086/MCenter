package com.astune.mcenter.ui;

import androidx.lifecycle.ViewModel;
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.object.Room.MCenterDB;
import io.reactivex.rxjava3.core.Completable;

public class DeviceCreationPageViewModel extends ViewModel {
    public Completable saveEvent(String name, String ip){
        return MCenterDB.Companion.getDB().deviceDao().insert(new Device(0, name, ip, null));
    }
    // TODO: Implement the ViewModel
}