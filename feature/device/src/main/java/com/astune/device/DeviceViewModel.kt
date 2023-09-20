package com.astune.device

import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceDataRepository: DeviceDataRepository,
    private val syncManager: SyncManager
): ViewModel() {
    private var devices by mutableStateOf(emptyList<Device>())
    val refresh = MutableStateFlow(false)

    fun getDeviceList(): List<Device> {
        viewModelScope.launch{
            deviceDataRepository.getDeviceList().collect{ value -> devices = value}
        }
        return devices
    }

    fun getDelay():StateFlow<Boolean>{
        if(!refresh.value)
        viewModelScope.launch {
            refresh.emit(true)
            Log.i("DeviceVM", "refreshing")
            syncManager.pingSync(devices.getIp()).stateIn(viewModelScope).collect(){
                for(device in devices){
                    val delay = it.outputData.getDouble(device.ip, (-1).toDouble()).toInt()
                    if (delay > -1){
                        device.delay = "$delay ms"
                        device.lastOnline = Instant.now().toString()
                    }else if (device.lastOnline == null) {
                        device.delay = "offline"
                    } else {
                        device.delay = getTimeBetween(
                            Instant.parse(device.lastOnline)
                        )
                    }
                }
                refresh.emit(false)
                Log.i("DeviceVM", "refreshing ended!")
            }
        }
        return refresh
    }

    fun stopPing(){
        syncManager.stopPing()
    }

    /*fun getDelay() {
        var delay: Int
        for (device in devices) {
            viewModelScope.launch {
                Log.i("Delay", "start detect")
                netWorkRepository.getAveragePing(device.ip, 5).collect() { delay1 ->
                    Log.i("Delay", "delay1:$delay1")
                    if (delay1.also { delay = it.toInt() } > -1) {
                        device.delay = "$delay ms"
                        device.lastOnline = Instant.now().toString()
                    } else {
                        netWorkRepository.getAveragePing(device.ip, 10).collect() { delay2 ->
                            Log.i("Delay", "delay2:$delay2")
                            if (delay2.also { delay = it.toInt() } > -1) {
                                device.delay = "$delay ms"
                                device.lastOnline = Instant.now().toString()
                            } else if (device.lastOnline == null) {
                                device.delay = "offline"
                            } else {
                                device.delay = getTimeBetween(
                                    Instant.parse(device.lastOnline)
                                )
                            }
                        }
                    }
                }
            }
        }
    }*/

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