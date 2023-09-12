package com.astune.mcenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.astune.core.ui.design.MCenterTheme
import com.astune.data.respository.UserDataRepository
import com.astune.mcenter.ui.McApp
import com.astune.model.UserInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ainActivity : ComponentActivity() {

    val viewModel : MainViewModel by viewModels()

    @Inject
    lateinit var userDataRepository: UserDataRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var userInfo by mutableStateOf(UserInfo())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.userInfo
                    .onEach {
                    userInfo = it
                    }
                    .collect()
            }
        }

        setContent {
            MCenterTheme (useDarkTheme = shouldUseDarkTheme(userInfo)) {
                McApp()
            }
        }
    }

    @Composable
    internal fun shouldUseDarkTheme(userInfo:UserInfo):Boolean{
        return when(userInfo.theme){
            "dark" -> true
            "light" -> false
            "default" -> isSystemInDarkTheme()

            else -> false
        }
    }
}