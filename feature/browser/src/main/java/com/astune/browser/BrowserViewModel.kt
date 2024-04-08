package com.astune.browser

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.core.network.repository.NetWorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val netWorkRepository:NetWorkRepository
): ViewModel() {
    private var isURLReachable by mutableStateOf(false)

    fun isURLReachable(url:String):Boolean{
        Log.d("WEB", url)
        viewModelScope.launch {
            netWorkRepository.isReachable(url, 1).collect(){
                Log.d("WEB", "result: $it")
                isURLReachable = it
            }
        }
        return isURLReachable
    }
}