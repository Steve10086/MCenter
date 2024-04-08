package com.astune.link.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.slideIn
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.astune.core.ui.ColumnWithTitleBarSpacer
import com.astune.link.LinkPanel
import com.astune.browser.WebLinkPage
import com.astune.sshclient.SshClientPanel

fun NavGraphBuilder.linkGraph(navController: NavController){
    navigation(startDestination = "linkPanel/{id}", route = "link/{id}"){
        composable(
            route = "linkPanel/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                nullable = false }),
            enterTransition = {
                Log.d("LinkNav", initialState.destination.route?:"")
                if (initialState.destination.route == "SSHLink/{address}/{id}"){
                    slideIn(initialOffset = { IntOffset(-it.width, 0) })
                }else{
                    slideIn(initialOffset = { IntOffset(it.width, 0) })
                }
            },
            exitTransition = {
                if (targetState.destination.route == "SSHLink/{address}/{id}"){
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }else{
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            ColumnWithTitleBarSpacer {
                LinkPanel(
                    parentId = it.arguments?.getInt("id") ?: -1,
                    onNavigationToSubApplication = {route -> navController.navigate(route)},
                )
            }
        }

        composable(
            route = "WebLink/{uri}",
            arguments = listOf(navArgument("uri") {
                type = NavType.StringType
                nullable = false}),
        ){
            WebLinkPage(
                uri = it.arguments?.getString("uri") ?:"",
                onExit = { navController.popBackStack() }
                )
        }

        composable(
            route = "SSHLink/{address}/{id}",
            arguments = listOf(
                navArgument("id") {
                type = NavType.IntType
                nullable = false},
                navArgument("address") {
                    type = NavType.StringType
                    nullable = false
                }
            ),
            enterTransition = {
                slideIn(initialOffset = { IntOffset(it.width, 0) })
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ){
            SshClientPanel(
                onExit = { navController.popBackStack() }
            )
        }
    }

}