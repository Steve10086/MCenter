package com.astune.link.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.slideIn
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.astune.link.LinkPanel
import com.astune.link.subPanels.WebLinkPage

fun NavGraphBuilder.linkGraph(navController: NavController){
    navigation(startDestination = "linkPanel/{id}", route = "link/{id}"){
        composable(
            route = "linkPanel/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                nullable = false }),
            enterTransition = {
                slideIn(initialOffset = { IntOffset(it.width, 0) })
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) { LinkPanel(
            parentId = it.arguments?.getInt("id") ?: -1,
            onNavigationToSubApplication = {route -> navController.navigate(route)},
        )
        }

        composable(
            route = "WebLink/{uri}",
            arguments = listOf(navArgument("uri") {
                type = NavType.StringType
                nullable = false}),
        ){
            WebLinkPage(
                uri = it.arguments?.getString("uri") ?:"0",
                onExit = { navController.popBackStack() }
                )
        }
    }

}