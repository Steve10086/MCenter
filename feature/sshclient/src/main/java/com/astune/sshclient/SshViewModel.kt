package com.astune.sshclient

import android.util.Log
import android.util.Size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.core.network.`object`.SshConnection
import com.astune.core.network.repository.NetWorkRepository
import com.astune.core.network.repository.SSHRepository
import com.astune.core.ui.design.SshThemes
import com.astune.data.respository.LinkDataRepository
import com.astune.data.respository.UserDataRepository
import com.astune.data.utils.ANSICommendDecoder
import com.astune.database.SSHLink
import com.astune.model.LinkType
import com.astune.model.ShellContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SshViewModel @Inject constructor(
    private val sshRepository: SSHRepository,
    private val netWorkRepository: NetWorkRepository,
    private val userDataRepository: UserDataRepository,
    private val linkDataRepository: LinkDataRepository,
    savedStateHandle: SavedStateHandle,
):ViewModel(){
    var onException by mutableStateOf(false)

    var delay by mutableStateOf(" ...")

    private val address = savedStateHandle.get<String>("address")

    var link:SSHLink? = null

    //get link from database
    init {
        viewModelScope.launch{
            Log.d("SSH", savedStateHandle.toString())
            if(savedStateHandle.get<Int>("id") != null){
                linkDataRepository.getLink(savedStateHandle.get<Int>("id")!!, LinkType.SSH_LINK)
                    .flowOn(Dispatchers.IO)
                    .collect(){
                        link = it as SSHLink
                        windowSize?.let { _ ->
                            connect(it)
                        }
                        viewModelScope.launch {
                            netWorkRepository.getAveragePing(address + ":" + link!!.info, 5000).collect {
                                delay = "$it ms"
                            }
                        }
                    }
            }else{
                onException = true
                Log.d("SSH", "Exception!")
            }
        }
    }

    var isLoading by mutableStateOf(true)
    private var content:ShellContent by mutableStateOf(ShellContent())
    private val decoder = ANSICommendDecoder(content)
    var displayText:AnnotatedString by mutableStateOf(AnnotatedString(""))
    private var windowSize:Size? = null

    //Initialize connection
    private var connection: SshConnection? = null

    private fun connect(link: SSHLink){
        Log.d("SSH", "connecting...")
        viewModelScope.launch(Dispatchers.IO){
            connection = sshRepository
                .connect(
                    address!!,
                    link.info.toInt(),
                    link.username,
                    link.password,
                    null,
                    null,
                    windowSize?.width,
                    windowSize?.height
                )
            content.windowsHeight = windowSize?.height?:0
            if (connection == null){
                Log.d("SSH", "connection failed!")
                onException = true
                return@launch
            }
            getShellContent()
            isLoading = false
        }
    }

    private fun getShellContent() {
        connection?.let {
            viewModelScope.launch {
                sshRepository.receive(it.shell)
                    .flowOn(Dispatchers.IO)
                    .collect(){
                        Log.d("SSHVM", it)
                        decoder.decodeCommend(it)
                        displayText = content.toAnnotatedString()
                    }
            }
        }
    }

    fun considerInsets() =
        content.pointer.second - content.bounds.first > content.windowsHeight / 2

    fun startWithWindowSize(size: Size){
        if (windowSize == null){
            Log.d("SSH", "windows Size Changed!")
            windowSize = size
            link?.let {
                viewModelScope.launch(Dispatchers.IO) {
                    connect(it)
                }
                Log.d("SSH", "connection established, current size $size")
                isLoading = false
            } ?: run{
                Log.d("SSH", "connection establishing, new size $size")
            }
        }
    }

    private var theme by mutableStateOf(SshThemes.black)
    fun getClientTheme():SshThemes{
        viewModelScope.launch(){
            theme = when(userDataRepository.getUserData().sshTheme){
                "black" -> SshThemes.black
                "violent" -> SshThemes.violent
                else -> SshThemes.black
            }
        }
        return theme
    }

    fun send(char: Char?){
        connection?.let {
            char?.let {c ->
                viewModelScope.launch {
                    if (!sshRepository.send(c.code.toByte() , it.shell)){
                        isLoading = true
                    }
                }
            }
        }
    }

    fun stop(){
        connection?.let {
            viewModelScope.launch(Dispatchers.IO) {
                sshRepository.disconnect(it)
                connection = null
            }
        }
    }


}

fun processKey(event: KeyEvent):Char? {
    if (event.key.keyCode in Key.A.keyCode..Key.Z.keyCode) {
        val c = event.utf16CodePoint.toChar()
        return if (event.isShiftPressed) {
            c.uppercaseChar()
        } else {
            c
        }
    }
    return when (event.key) {
        Key.ShiftLeft, Key.ShiftRight -> null
        Key.Backspace -> null
        else -> event.utf16CodePoint.toChar()
    }
}
