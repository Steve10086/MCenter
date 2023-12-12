package com.astune.data.fake

import com.astune.common.Dispatcher
import com.astune.common.Dispatchers
import com.astune.database.Device
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FakeDataRepository @Inject constructor(
    @Dispatcher(Dispatchers.IO)private val ioDispatcher: CoroutineDispatcher,
){
    private val deviceList:List<Device> = listOf(
        Device(1, "device1", "127.0.0.1", null, null)
    )
    fun getDevice(id:Int): Flow<Device> {
        return flow{emit(deviceList[0])}.flowOn(ioDispatcher)
    }

    fun getDeviceDelay(id:Int): Flow<String> {
        return flow{emit("0")}.flowOn(ioDispatcher)
    }

    fun getDeviceList(): Flow<List<Device>> {
        return flow {emit(deviceList)}.flowOn(ioDispatcher)
    }

    suspend fun updateLatestDelayByIp(ip:String, delay:String){
    }

    suspend fun insertDevice(device: Device){
    }

    suspend fun deleteDevice(device: Device){
    }
}