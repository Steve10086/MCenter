package com.astune.core.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.astune.core.network.repository.NetWorkRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PingSynchronizer @AssistedInject constructor(
    @Assisted ctx:Context,
    @Assisted params: WorkerParameters,
    private val networkRepository: NetWorkRepository,
) : CoroutineWorker(
    ctx,
    params,) {
    override suspend fun doWork(): Result {
        val ipList = inputData.keyValueMap["ip"] as List<String>
        val results = mutableMapOf<String, Double>()
        for (ip in ipList){
            networkRepository.getAveragePing(ip, 5).collect(){
                if (it == (-1).toDouble()){
                    networkRepository.getAveragePing(ip, 10).collect(){delay ->
                        results[ip] = delay
                    }
                }else{
                    results[ip] = it
                }
            }
        }
        val resultData = workDataOf(*results.toMap().toList().map { it }.toTypedArray())
        return Result.success(resultData)
    }

}