package com.astune.core.network.repository

import com.astune.common.Dispatcher
import com.astune.common.Dispatchers
import com.astune.core.network.utils.averagePing
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class NetWorkRepository @Inject constructor(
     @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher
){
    suspend fun getAveragePing(url: String, timeout: Int): Flow<Double> {
        return flow{emit(averagePing(url, timeout))}.flowOn(ioDispatcher)
    }
}