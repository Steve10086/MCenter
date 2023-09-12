package com.astune.link

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.data.respository.DeviceDataRepository
import com.astune.data.respository.LinkDataRepository
import com.astune.database.Device
import com.astune.database.Link.Link
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkViewModel @Inject constructor(
    private val linkDataRepository: LinkDataRepository,
    private val deviceDataRepository: DeviceDataRepository
) : ViewModel(){
    private var linkList by mutableStateOf(emptyList<Link>())
    private var device by mutableStateOf(Device(0, "0", "0", null))

    fun getParentDevice(id:Int):Device{
        viewModelScope.launch {
            deviceDataRepository.getDevice(id).collect(){device = it}
        }
        return device
    }

    fun getLinkList(parentId:Int):List<Link>{
        viewModelScope.launch {
            getList(parentId)
        }
        return linkList
    }

    fun insertLink(link: Link){
        viewModelScope.launch {
            linkDataRepository.insertLink(link)
            getList(link.parent)
        }
    }

    fun deleteLink(link: Link){
        viewModelScope.launch{
            linkDataRepository.deleteLink(link)
            getList(link.parent)
        }
    }

    private suspend fun getList(parentId: Int){
        val list = mutableListOf<Link>()
        linkDataRepository.getLinkList(parentId).collect{ list += it }
        linkList = list
    }

}