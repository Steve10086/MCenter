package com.astune.core.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.astune.core.network.repository.NetWorkRepository
import com.astune.core.sync.MCWorkManager.SyncManager
import com.astune.data.respository.DeviceDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

@HiltWorker
class PingSynchronizer @AssistedInject constructor(
    @Assisted ctx:Context,
    @Assisted params: WorkerParameters,
    private val networkRepository: NetWorkRepository,
    private val syncManager: SyncManager
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val ipList = inputData.getStringArray("ip")?: emptyArray()
        val results = mutableMapOf<String, Int>()
        runBlocking {
            for (ip in ipList){
                launch {
                    networkRepository.getAveragePing(ip, 5).collect {
                        if (it == (-1).toDouble()){
                            networkRepository.getAveragePing(ip, 10).collect { delay ->
                                results[ip] = delay.roundToInt()
                            }
                        }else{
                            results[ip] = it.roundToInt()
                        }
                        val resultData = workDataOf(*results.toList().toTypedArray())
                        setProgress(resultData)
                    }
                }
            }
        }
        delay(1000)
        syncManager.restorePingResult(workDataOf(*results.toList().toTypedArray(),Pair("ip", ipList)))
        return Result.success()
    }
}

@HiltWorker
class SynchronizeToDataBase @AssistedInject constructor(
    @Assisted ctx:Context,
    @Assisted params: WorkerParameters,
    private val deviceDataRepository: DeviceDataRepository
) : CoroutineWorker(ctx, params){
    override suspend fun doWork(): Result {
        val ipList = inputData.getStringArray("ip")?: emptyArray()
        for (ip in ipList){
            try{
                deviceDataRepository.updateLatestDelayByIp(ip, inputData.keyValueMap[ip].toString())
            }catch (ignore:NullPointerException){}
        }
        return Result.success()
    }
}