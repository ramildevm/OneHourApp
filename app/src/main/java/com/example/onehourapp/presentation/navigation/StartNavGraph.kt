package com.example.onehourapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.onehourapp.presentation.start.StartScreen

fun NavGraphBuilder.startNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.START,
        startDestination = StartScreen.Main.route
    ) {
        composable(route = StartScreen.Main.route) {
            StartScreen(
                onStartBtnClick = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)
                }
            )
        }
    }
}
enum class StartScreen(val route: String) {
     Main(route = "MAIN")
}