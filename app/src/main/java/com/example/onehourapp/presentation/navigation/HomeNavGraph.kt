package com.example.onehourapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.onehourapp.presentation.home.BottomBar
import com.example.onehourapp.settings.presentation.SettingsContent

@Composable
fun HomeNavGraph(navController: NavHostController, isChanged: MutableState<Int>) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBar.Calendar.route
    ) {
        composable(route = BottomBar.YearStat.route) {
            //YearStatScreenContent(navController)
        }
        composable(route = BottomBar.Calendar.route) {
            //com.example.onehourapp.calendar.CalendarScreenContent(navController, isChanged)
        }
        composable(route = BottomBar.Activity.route) {
            com.example.onehourapp.activities.presentation.ActivityScreenContent(navController)
        }
        composable(route = BottomBar.Settings.route) {
            com.example.onehourapp.settings.presentation.SettingsContent()
        }
        detailsNavGraph(navController = navController)
    }
}

@Composable
fun ScreenContent(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(com.example.onehourapp.theme.ui.BackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.clickable { onClick() },
            text = name,
            color = White,
            fontSize = MaterialTheme.typography.h3.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailsScreen.Information.route
    ) {
        composable(route = DetailsScreen.Information.route) {

        }
        composable(route = DetailsScreen.Overview.route) {
            ScreenContent(name = DetailsScreen.Overview.route) {
                navController.popBackStack(
                    route = DetailsScreen.Information.route,
                    inclusive = false
                )
            }
        }
    }
}

sealed class DetailsScreen(val route: String) {
    object Information : DetailsScreen(route = "INFORMATION")
    object Overview : DetailsScreen(route = "OVERVIEW")
}