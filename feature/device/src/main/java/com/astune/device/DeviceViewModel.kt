package com.astune.device

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.data.respository.DeviceDataRepository
import com.astune.database.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceDataRepository: DeviceDataRepository,
): ViewModel() {
    private var devices by mutableStateOf(emptyList<Device>())

    fun getDeviceList(): List<Device> {
        viewModelScope.launch{
            deviceDataRepository.getDeviceList().collect{ value -> devices = value }
        }
        return devices
    }

    fun delete(device: Device){
        viewModelScope.launch {
            deviceDataRepository.deleteDevice(device)
            deviceDataRepository.getDeviceList().collect{ value -> devices = value }
        }
    }

    fun insert(device: Device){
        viewModelScope.launch {
            deviceDataRepository.insertDevice(device)
            deviceDataRepository.getDeviceList().collect{ value -> devices = value }
        }
    }
}