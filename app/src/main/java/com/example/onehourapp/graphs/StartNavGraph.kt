package com.example.onehourapp.graphs

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.onehourapp.screens.StartContent
import com.example.onehourapp.utils.SharedReferencesUtil

fun NavGraphBuilder.startNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.START,
        startDestination = StartScreen.Main.route
    ) {
        composable(route = StartScreen.Main.route) {
            StartContent(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)
                }
            )
        }
    }
}
sealed class StartScreen(val route: String) {
    object Main : StartScreen(route = "MAIN")
}