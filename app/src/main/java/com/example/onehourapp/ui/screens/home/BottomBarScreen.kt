package com.example.onehourapp.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.*
import com.example.onehourapp.R
import com.example.onehourapp.ui.helpers.IconResource

sealed class BottomBarScreen(
    val route: String,
    val title: Int,
    val icon: IconResource,
) {
    object YearStat : BottomBarScreen(
        route = "YEARSTAT",
        title = R.string.year_stats,
        icon = IconResource.fromDrawableResource(R.drawable.year_stat_icon)
    )

    object Calendar : BottomBarScreen(
        route = "CALENDAR",
        title = R.string.calendar,
        icon = IconResource.fromImageVector(Icons.Rounded.CalendarToday)
    )

    object Add : BottomBarScreen(
        route = "ADD",
        title = 0,
        icon = IconResource.fromImageVector(Icons.Rounded.Add)
    )

    object Activity : BottomBarScreen(
        route = "ACTIVITY",
        title = R.string.activity,
        icon = IconResource.fromImageVector(Icons.Rounded.ViewList)
    )

    object Settings : BottomBarScreen(
        route = "SETTINGS",
        title = R.string.settings,
        icon = IconResource.fromImageVector(Icons.Rounded.Settings)
    )
}