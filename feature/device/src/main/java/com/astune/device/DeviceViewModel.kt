package com.astune.device

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.astune.core.sync.MCWorkManager.SyncManager
import com.astune.data.respository.DeviceDataRepository
import com.astune.data.utils.getTimeBetween
import com.astune.database.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceDataRepository: DeviceDataRepository,
    private val syncManager: SyncManager
): ViewModel() {
    var deviceFlow = MutableStateFlow(emptyList<Device>())
    private var refreshing = false
    private val pingDisPatchers = Dispatchers.IO

    init{
        getDeviceList()
    }

    private fun getDeviceList(){
        viewModelScope.launch(pingDisPatchers){
            deviceDataRepository.getDeviceList().collect{ value ->
                deviceFlow.value = value
                getDelay()
            }
        }
    }


    fun ping(ips:List<String>){
        Log.i("DeviceVM", ips.toString())
        syncManager.pingSync(ips)
    }

    fun getDelay(devices: List<Device> = deviceFlow.value){
        if(!refreshing && devices.isNotEmpty()){
            viewModelScope.launch(pingDisPatchers) {
                refreshing = true
                syncManager.getLastPing().collect(){
                    //Log.i("DeviceVM", "getting Delay!")
                    devices.setLoading(true)
                    for(device in devices){
                        var delay = "-1"

                        if(it.state == WorkInfo.State.RUNNING){
                            delay = it.progress.getInt(device.ip, -2).toString()
                        }else{
                            deviceDataRepository.getDeviceDelay(device.id).collect{value ->
                                if(value != null) delay = value
                            }
                        }

                        when(delay.toInt()){
                            -2 -> continue

                            -1 -> {
                                if(device.lastOnline == null){
                                    device.delay = "offline"
                                }else{
                                    device.delay = getTimeBetween(
                                        Instant.parse(device.lastOnline)
                                    )
                                }
                            }

                            else -> {
                                device.delay = "$delay ms"
                                device.lastOnline = Instant.now().toString()
                            }
                        }
                        device.loading = false
                    }
                    refreshing = false
                }
            }
        }
    }

    fun stopPing(devices: List<Device>){
        devices.setLoading(false)
        syncManager.stopPing()
    }

    fun delete(device: Device){
        viewModelScope.launch(Dispatchers.Main) {
            deviceDataRepository.deleteDevice(device)
            getDeviceList()
        }
    }

    fun insert(device: Device){
        viewModelScope.launch {
            deviceDataRepository.insertDevice(device)
            getDeviceList()
        }
    }
}

fun List<Device>.getIp():List<String>{
    return mutableListOf<String>().apply {
        for(device in this@getIp){
            this.add(device.ip)
        }
    }
}

internal fun List<Device>.setLoading(state:Boolean){
    for(device in this@setLoading){
        device.loading = state
    }
}