package com.astune.core.sync.MCWorkManager

import android.content.Context
import androidx.lifecycle.Observer
import androidx.work.*
import com.astune.core.sync.PingSynchronizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncManager @Inject constructor(@ApplicationContext private val ctx : Context){

    private val workManager = WorkManager.getInstance(ctx)

    private val mutableStateFlow = MutableStateFlow<Map<String, Double>>(emptyMap())

    private val workObserver:Observer<List<WorkInfo>> = Observer{results ->
        mutableStateFlow.value =
            results[0].outputData.keyValueMap as Map<String, Double>}

    fun pingSync(ip: List<String>): StateFlow<Map<String, Double>> {

        val syncWork = PeriodicWorkRequestBuilder<PingSynchronizer>(15, TimeUnit.MINUTES).setInputData(workDataOf("ip" to ip))
            .build()

        val result:StateFlow<Map<String, Double>> = mutableStateFlow

        workManager.enqueueUniquePeriodicWork(
            PingSync,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            syncWork
        )
        workManager.getWorkInfosByTagLiveData(PingSync).observeForever(workObserver)

        return result
    }

    fun stopPing(){
        workManager.getWorkInfosByTagLiveData(PingSync).removeObserver(workObserver)
        workManager.cancelUniqueWork(PingSync)
    }
}

internal const val PingSync = "PingSync"