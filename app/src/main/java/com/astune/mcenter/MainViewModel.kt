package com.astune.mcenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astune.data.respository.UserDataRepository
import com.astune.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userDataRepository: UserDataRepository
): ViewModel(){
    var userInfo : StateFlow<UserInfo> = userDataRepository.userData.stateIn(
        scope = viewModelScope,
        initialValue = UserInfo(),
        started = SharingStarted.WhileSubscribed(5_000),
    )

}