package com.astune.data.respository

import com.astune.common.Dispatcher
import com.astune.common.Dispatchers.IO
import com.astune.database.Device
import com.astune.database.dao.DeviceDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeviceDataRepository @Inject constructor(
    private val deviceDao: DeviceDao,
    @Dispatcher(IO)private val ioDispatcher: CoroutineDispatcher,
){
    fun getDevice(id:Int): Flow<Device>{
        return flow{emit(deviceDao.getDevice(id))}.flowOn(ioDispatcher)
    }

    fun getDeviceList(): Flow<List<Device>> {
        return flow {emit(deviceDao.getAll())}.flowOn(ioDispatcher)
    }


    suspend fun insertDevice(device: Device){
        deviceDao.insert(device)
    }

    suspend fun deleteDevice(device: Device){
        deviceDao.delete(device)
    }
}