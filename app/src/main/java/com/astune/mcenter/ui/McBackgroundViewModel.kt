package com.astune.mcenter.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.data.respository.UserDataRepository
import com.astune.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class McBackgroundViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
): ViewModel() {
    private var userInfo = mutableStateOf(UserInfo())

    fun getUserInfo() : UserInfo {
        viewModelScope.launch {
            userInfo.value = userDataRepository.getUserData()
        }
        return userInfo.value
    }
}