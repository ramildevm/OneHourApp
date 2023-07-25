package com.example.onehourapp.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.onehourapp.screens.home.HomeScreen
import com.example.onehourapp.utils.SharedPreferencesUtil


@Composable
fun RootNavigationGraph(navController: NavHostController) {
    val context = LocalContext.current
    val authStatus = SharedPreferencesUtil.getSharedStringData(context,"auth_status")

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = if(authStatus=="") Graph.START else Graph.HOME
    ) {
        startNavGraph(navController = navController)
        composable(route = Graph.HOME) {
            HomeScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val START = "start_graph"
    const val HOME = "home_graph"
    const val DETAILS = "details_graph"
}