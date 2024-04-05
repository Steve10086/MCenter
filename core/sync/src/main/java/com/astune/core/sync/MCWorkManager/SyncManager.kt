package com.astune.core.sync.MCWorkManager

import android.content.Context
import androidx.work.*
import com.astune.core.sync.PingSynchronizer
import com.astune.core.sync.SynchronizeToDataBase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncManager @Inject constructor(
    @ApplicationContext private val ctx : Context,
){

    private val workManager = WorkManager.getInstance(ctx)

    /**
     * start ping work
     * */
   fun pingSync(ip: List<String>){
        val syncWork = PeriodicWorkRequestBuilder<PingSynchronizer>(15, TimeUnit.MINUTES)
            .setInputData(workDataOf("ip" to ip.toTypedArray()))
            .build()

        workManager.enqueueUniquePeriodicWork(
            PingSyncName,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            syncWork
        )
    }

    fun getLastPing(): Flow<WorkInfo> = workManager.getWorkInfosForUniqueWorkFlow(PingSyncName).map {
        return@map it[0]
    }

    fun doSinglePing(ip: List<String>): Flow<WorkInfo> {
        val syncWork = OneTimeWorkRequestBuilder<PingSynchronizer>(
        ).setInputData(workDataOf("ip" to ip.toTypedArray()))
            .addTag(SinglePingName)
            .build()
        workManager.enqueue(syncWork)

        return workManager.getWorkInfosByTagFlow(SinglePingName).map {
            return@map it[0]
        }
    }

    fun restorePingResult(restoreData:Data){
        val syncWork = OneTimeWorkRequestBuilder<SynchronizeToDataBase>(
        ).setInputData(restoreData)
            .addTag(RestorePingName)
            .build()
        workManager.enqueue(syncWork)
    }

    fun stopPing(){
        workManager.cancelUniqueWork(PingSyncName)
    }
}

internal const val PingSyncName = "PingSync"
internal const val SinglePingName = "SinglePingSync"
internal const val RestorePingName = "RestorePingResult"
