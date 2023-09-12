package com.astune.datastore

import androidx.datastore.core.DataStore
import com.astune.model.UserInfo
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val dataStore: DataStore<UserPreferences>
){
    val userData = dataStore.data.map{
        UserInfo(
            name = it.name,
            email = it.email,
            enabledZerotier = it.enableZerotier,
            zerotierPass = it.zerotierPass,
            theme = it.theme
            )
    }

    suspend fun setUserData(userInfo: UserInfo){
        dataStore.updateData {
            it.
                toBuilder().
                clear().
                setEmail(userInfo.email).
                setEnableZerotier(userInfo.enabledZerotier).
                setName(userInfo.name).
                setTheme(userInfo.theme).
                setZerotierPass(userInfo.zerotierPass)
            .build()
        }
    }

    suspend fun setTheme(theme:String){
        dataStore.updateData {
            it.
            toBuilder().
            clearTheme().
            setTheme(theme).
            build()
        }
    }
}