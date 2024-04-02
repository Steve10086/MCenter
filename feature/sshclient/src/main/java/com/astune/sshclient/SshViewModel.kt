package com.astune.sshclient

import android.util.Log
import android.util.Size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.core.network.`object`.SshConnection
import com.astune.core.network.repository.NetWorkRepository
import com.astune.core.network.repository.SSHRepository
import com.astune.core.ui.CustomKey
import com.astune.core.ui.CustomKey.*
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
            //Log.d("SSH", savedStateHandle.toString())
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
    private var keyState:MutableMap<CustomKey, Boolean> = mutableMapOf()

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
                val fChar = getCompositeChar(c)
                viewModelScope.launch {
                    if (!sshRepository.send(fChar.toString().toByteArray() , it.shell)){
                        isLoading = true
                    }
                }
            }
        }
    }

    fun sendArray(array: ByteArray){
        connection?.let {
            viewModelScope.launch {
                if(!sshRepository.send(array, it.shell)){
                    isLoading = true
                }
            }
        }
    }

    fun onKeyClicked(name: CustomKey, oldState:Boolean):Boolean{
        when(name){
            Ctrl -> {
                keyState[name] = !oldState
                return !oldState
            }
            Esc -> send((27).toChar())
            Tab -> send((9).toChar())
            Alt -> sendArray("\u001B[".toByteArray())
            Up -> sendArray("\u001B[A".toByteArray())
            Down -> sendArray("\u001B[B".toByteArray())
            Left -> sendArray("\u001B[D".toByteArray())
            Right -> sendArray("\u001B[C".toByteArray())
        }
        return false
    }

    fun stop(){
        connection?.let {
            viewModelScope.launch(Dispatchers.IO) {
                sshRepository.disconnect(it)
                connection = null
            }
        }
    }

    fun getCompositeChar(char: Char): Char {
        if(keyState[Ctrl] == true){
            return when(char){
                'c' -> (3).toChar() //EOT
                'd' -> (4).toChar() //EOF
                'z' -> (26).toChar()//Substitute
                'l' -> (12).toChar()//ClearScreen
                'u' -> (21).toChar()//Delete line
                'w' -> (23).toChar()//Delete word
                'r' -> (18).toChar()//Search history
                'a' -> (1).toChar() //Move to start
                'e' -> (5).toChar() //Move to end
                'k' -> (11).toChar()//Delete from cursor to end
                'y' -> (25).toChar()//Restore
                't' -> (20).toChar()//Swap last two letter
                else -> char
            }
        }
        return char
    }


}
