package com.astune.sshclient

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.core.network.`object`.SshConnection
import com.astune.core.network.repository.SSHRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SshViewModel @Inject constructor(
    private val sshRepository: SSHRepository,
    savedStateHandle: SavedStateHandle,
):ViewModel(){
    private val address = savedStateHandle.get<String>("address")
    private val port = savedStateHandle.get<Int>("port")
    private val username = savedStateHandle.get<String>("username")
    private val password = savedStateHandle.get<String>("password")

    var isLoading by mutableStateOf(true)
    var onException by mutableStateOf(false)
    var defaultText by mutableStateOf("")

    //Initialize connection
    private val connection by lazy {
        var connection: SshConnection? = null
        viewModelScope.launch{
            if(address != null
                && port != null
                && username != null
                && password != null){
                connection = sshRepository
                    .connect(
                        address,
                        port,
                        username,
                        password
                    )
                isLoading = false
            }else{
                onException = true
            }
        }
        connection
    }

    fun getShellContent(): String {
        connection?.let {
            viewModelScope.launch {
                sshRepository.receive(it.shell).collect(){
                    val content = it
                }
            }
        }
        return ""
    }


}