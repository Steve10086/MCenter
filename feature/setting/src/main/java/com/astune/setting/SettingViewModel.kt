package com.astune.setting

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.data.respository.UserDataRepository
import com.astune.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
): ViewModel() {
    private var userInfo = UserInfo()

    fun updateTheme(theme:String){
        viewModelScope.launch {
            userDataRepository.setTheme(theme)
        }
    }

    fun updateAvatar(uri: Uri):Bitmap{
        return userDataRepository.setAvatar(uri)
    }
    fun updateUserInfo(userInfo: UserInfo){
        viewModelScope.launch {
            userDataRepository.setUserData(userInfo)
        }
    }

    fun getInfo() : UserInfo{
        viewModelScope.launch {
            userInfo = userDataRepository.getUserData()
        }
        return userInfo
    }

}