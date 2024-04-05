package com.astune.mcenter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.astune.core.sync.MCWorkManager.SyncManager
import com.astune.core.ui.design.MCenterTheme
import com.astune.data.respository.UserDataRepository
import com.astune.device.getIp
import com.astune.mcenter.ui.McApp
import com.astune.model.UserInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel : MainViewModel by viewModels()

    @Inject
    lateinit var userDataRepository: UserDataRepository

    @Inject
    lateinit var syncManager: SyncManager


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
            val darkTheme = shouldUseDarkTheme(userInfo)

            DisposableEffect(darkTheme){
                enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },)
                onDispose {}
            }


            MCenterTheme (useDarkTheme = darkTheme) {
                McApp()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        syncManager.stopPing()
        Log.d("Main", "Sync paused")
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.device.collect() {
                syncManager.pingSync(it.getIp())
            }
            Log.d("Main", "Sync start")
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

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)