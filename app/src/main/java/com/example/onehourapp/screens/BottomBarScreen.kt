package com.example.onehourapp.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Calendar : BottomBarScreen(
        route = "CALENDAR",
        title = "Calendar",
        icon = Icons.Rounded.CalendarToday
    )

    object Activity : BottomBarScreen(
        route = "ACTIVITY",
        title = "Activity",
        icon = Icons.Rounded.ListAlt
    )

    object Settings : BottomBarScreen(
        route = "SETTINGS",
        title = "Settings",
        icon = Icons.Rounded.Settings
    )
}