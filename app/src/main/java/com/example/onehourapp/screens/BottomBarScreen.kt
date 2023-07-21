package com.example.onehourapp.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import com.example.onehourapp.R
import com.example.onehourapp.helpers.IconResource

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: IconResource,
) {
    object YearStat : BottomBarScreen(
        route = "YEARSTAT",
        title = "Year stats",
        icon = IconResource.fromDrawableResource(R.drawable.year_stat_icon)
    )

    object Calendar : BottomBarScreen(
        route = "CALENDAR",
        title = "Calendar",
        icon = IconResource.fromImageVector(Icons.Rounded.CalendarToday)
    )

    object Add : BottomBarScreen(
        route = "ADD",
        title = "",
        icon = IconResource.fromDrawableResource(R.drawable.add_circle_icon)
    ), AddButtonScreen

    object Activity : BottomBarScreen(
        route = "ACTIVITY",
        title = "Activity",
        icon = IconResource.fromImageVector(Icons.Rounded.ViewList)
    )

    object Settings : BottomBarScreen(
        route = "SETTINGS",
        title = "Settings",
        icon = IconResource.fromImageVector(Icons.Rounded.Settings)
    )
}
interface AddButtonScreen