package com.example.onehourapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.onehourapp.common.preferences.SharedPreferencesKeys
import com.example.onehourapp.presentation.home.HomeScreen
import com.example.onehourapp.common.utils.SharedPreferencesUtil

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    val context = LocalContext.current
    val authStatus = SharedPreferencesUtil.getSharedStringData(context, SharedPreferencesKeys.PREF_AUTH_STATUS)

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