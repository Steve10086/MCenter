package com.astune.core.sync.MCWorkManager

import android.content.Context
import androidx.work.*
import com.astune.core.sync.PingSynchronizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncManager @Inject constructor(@ApplicationContext private val ctx : Context){

    private val workManager = WorkManager.getInstance(ctx)

   fun pingSync(ip: List<String>): Flow<WorkInfo> {

        val syncWork = PeriodicWorkRequestBuilder<PingSynchronizer>(
            15,
            TimeUnit.MINUTES
        ).setInputData(workDataOf("ip" to ip.toTypedArray()))
            .build()

        workManager.enqueueUniquePeriodicWork(
            PingSyncName,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            syncWork
        )
        return workManager.getWorkInfosForUniqueWorkFlow(PingSyncName).map {
            return@map it[0]
        }

    }

    fun stopPing(){
        workManager.cancelUniqueWork(PingSyncName)
    }
}

internal const val PingSyncName = "PingSync"