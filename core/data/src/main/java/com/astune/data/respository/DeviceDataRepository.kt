package com.astune.data.respository

import com.astune.common.Dispatcher
import com.astune.common.Dispatchers.IO
import com.astune.database.Device
import com.astune.mcenter.`object`.Dao.DeviceDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class DeviceDataRepository @Inject constructor(
    private val deviceDao:DeviceDao,
    @Dispatcher(IO)private val ioDispatcher: CoroutineDispatcher,
){
    private val deviceFlow = MutableStateFlow<List<Device>>(listOf())

    fun getDeviceList(): Flow<List<Device>> {
        return deviceFlow
    }

    fun sendDeviceList(){
        deviceFlow.tryEmit(deviceDao.getAll())
    }

    suspend fun insertDevice(device: Device){
        deviceDao.insert(device)
    }

    suspend fun deleteDevice(device: Device){
        deviceDao.insert(device)
    }
}