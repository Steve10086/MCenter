package com.astune.device

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.core.sync.MCWorkManager.SyncManager
import com.astune.data.respository.DeviceDataRepository
import com.astune.data.utils.getTimeBetween
import com.astune.database.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceDataRepository: DeviceDataRepository,
    private val syncManager: SyncManager
): ViewModel() {
    var devices by mutableStateOf(emptyList<Device>())
    private var refresh by mutableStateOf(false)

    init{
        getDeviceList()
    }

    private fun getDeviceList(){
        viewModelScope.launch{
            deviceDataRepository.getDeviceList().collect{ value ->
                devices = value
                getDelay()
            }
        }
    }

    fun getDelay(){
        if(!refresh && devices.isNotEmpty()){
            devices.setLoading()
            viewModelScope.launch {
                refresh = true
               // Log.i("DeviceVM", "refreshing")
                syncManager.pingSync(devices.getIp()).stateIn(viewModelScope).collect(){
                    val resultMap = it.progress.keyValueMap
                    if (resultMap.isNotEmpty()){
                        //Log.i("DeviceVM", resultMap.toString())
                        for(device in devices){
                            val delay = it.progress.getDouble(device.ip, (-2).toDouble()).toInt()
                            if(delay == -2){
                                continue
                            } else if (delay > -1){
                                device.delay = "$delay ms"
                                device.lastOnline = Instant.now().toString()
                            }else if (device.lastOnline == null) {
                                device.delay = "offline"
                            } else {
                                device.delay = getTimeBetween(
                                    Instant.parse(device.lastOnline)
                                )
                            }
                            device.loading = false
                        }
                        refresh = false
                        //Log.i("DeviceVM", "refreshing ended!")
                    }
                }
            }
        }
    }

    fun stopPing(){
        syncManager.stopPing()
    }


    fun delete(device: Device){
        viewModelScope.launch {
            deviceDataRepository.deleteDevice(device)
            deviceDataRepository.getDeviceList().collect{ value -> devices = value}
        }
    }

    fun insert(device: Device){
        viewModelScope.launch {
            deviceDataRepository.insertDevice(device)
            deviceDataRepository.getDeviceList().collect{ value -> devices = value }
        }
    }

}

internal fun List<Device>.getIp():List<String>{
    return mutableListOf<String>().apply {
        for(device in this@getIp){
            this.add(device.ip)
        }
    }
}

internal fun List<Device>.setLoading(){
    for(device in this@setLoading){
        device.loading = true
    }
}