package com.astune.mcenter.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.astune.core.ui.TitleBarValues


@Composable
fun rememberMCAppState(
    navHostController: NavHostController = rememberNavController(),
):MCAppState = remember(
    navHostController,
){ MCAppState(
    navHostController = navHostController,
) }

@Stable
class MCAppState (
    val navHostController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navHostController.currentBackStackEntryAsState().value?.destination

    fun getTitleBarValues(route: String?):TitleBarValues =
        when(route){
            "device" -> TitleBarValues.DEVICE
            "setting" -> TitleBarValues.SETTING
            "linkPanel/{id}" -> TitleBarValues.LINK
            else -> TitleBarValues.Default
        }

    fun navigateTo(route: String){
        navHostController.navigate(route)
    }

    fun onRightBtnClicked(
        des:String
    ){
        when(des){
            "setting" -> navigateTo("device")
            else -> {}
        }
    }

    fun onLeftBtnClicked(des:String){
        when(des){
            "setting" -> navigateTo("device")
            "linkPanel/{id}" -> navigateTo("device")
        }
    }
}