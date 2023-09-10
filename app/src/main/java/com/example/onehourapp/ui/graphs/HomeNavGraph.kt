package com.example.onehourapp.ui.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.onehourapp.ui.screens.BottomBarScreen
import com.example.onehourapp.ui.screens.home.settings.SettingsContent
import com.example.onehourapp.ui.screens.home.yearstat.YearStatScreenContent
import com.example.onehourapp.ui.screens.home.activity.ActivityScreenContent
import com.example.onehourapp.ui.screens.home.calendar.CalendarScreenContent
import com.example.onehourapp.ui.theme.BackgroundColor

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Activity.route
    ) {

        composable(route = BottomBarScreen.YearStat.route) {
            YearStatScreenContent(navController)
        }
        composable(route = BottomBarScreen.Calendar.route) {
            CalendarScreenContent(navController)
        }
        composable(route = BottomBarScreen.Activity.route) {
            ActivityScreenContent(navController)
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsContent()
        }
        detailsNavGraph(navController = navController)
    }
}

@Composable
fun ScreenContent(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
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