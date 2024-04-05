package com.astune.mcenter.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.astune.core.ui.ColumnWithTitleBarSpacer
import com.astune.device.DevicePanel
import com.astune.link.navigation.linkGraph
import com.astune.mcenter.ui.MCAppState
import com.astune.setting.SettingPanel

@Composable
fun McNavHost(
    mcAppState: MCAppState,
    modifier: Modifier = Modifier
){
    val navHostController = mcAppState.navHostController

    NavHost(modifier = modifier, navController = navHostController, startDestination = "device"){

        composable("setting") {
            ColumnWithTitleBarSpacer{
                SettingPanel()
            }
        }

        composable(
            route = "device",
            exitTransition = {
                if(targetState.destination.route == "linkPanel/{id}"){
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }else{
                    fadeOut()
                }
            },
            enterTransition = {
                if(initialState.destination.route == "linkPanel/{id}"){
                    slideIn(initialOffset = { IntOffset(-it.width, 0) })
                }else{
                    fadeIn()
                }
            }
        ) {
            ColumnWithTitleBarSpacer {
                DevicePanel(
                    onNavigateToLink = { navHostController.navigate("linkPanel/$it")},
                )
            }
        }

        linkGraph(navHostController)
    }
}