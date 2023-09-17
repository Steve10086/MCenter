package com.astune.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.core.network.repository.NetWorkRepository
import com.astune.data.respository.DeviceDataRepository
import com.astune.database.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceDataRepository: DeviceDataRepository,
    private val netWorkRepository: NetWorkRepository,
): ViewModel() {
    private var devices by mutableStateOf(emptyList<Device>())

    fun getDeviceList(): List<Device> {
        viewModelScope.launch{
            deviceDataRepository.getDeviceList().collect{ value -> devices = value}
        }
        getDelay()
        return devices
    }

    fun getDelay(){
        var delay: Int
        for (device in devices){
            viewModelScope.launch {
                Log.i("Delay", "start detect")
                netWorkRepository.getAveragePing(device.ip, 5).collect() {delay1 ->
                    Log.i("Delay", "delay1:$delay1")
                    if (delay1.also { delay = it.toInt() } > -1) {
                        device.delay = "$delay ms"
                        device.lastOnline = null
                    } else {
                        netWorkRepository.getAveragePing(device.ip, 10).collect(){delay2 ->
                            Log.i("Delay", "delay2:$delay2")
                            if(delay2.also { delay = it.toInt() } > -1){
                                device.delay = "$delay ms"
                                device.lastOnline = null
                            } else {
                                device.delay =
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(
                                        Instant.ofEpochSecond(Duration.between(
                                            Instant.now(),
                                            Instant.parse(device.lastOnline?:Instant.now().toString())
                                        ).seconds
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun onExit(){
        for (device in devices){
            device.lastOnline?.let{device.lastOnline = Instant.now().toString()}
        }
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