package com.example.onehourapp.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import com.example.onehourapp.R

sealed class BottomBar(
    val route: String,
    val title: Int,
    val icon: com.example.onehourapp.presentation.helpers.IconResource,
) {
    object YearStat : BottomBar(
        route = "YEARSTAT",
        title = R.string.year_stats,
        icon = com.example.onehourapp.presentation.helpers.IconResource.fromDrawableResource(R.drawable.year_stat_icon)
    )

    object Calendar : BottomBar(
        route = "CALENDAR",
        title = R.string.calendar,
        icon = com.example.onehourapp.presentation.helpers.IconResource.fromImageVector(Icons.Rounded.CalendarToday)
    )

    object Add : BottomBar(
        route = "ADD",
        title = 0,
        icon = com.example.onehourapp.presentation.helpers.IconResource.fromImageVector(Icons.Rounded.Add)
    )

    object Activity : BottomBar(
        route = "ACTIVITY",
        title = R.string.activities,
        icon = com.example.onehourapp.presentation.helpers.IconResource.fromImageVector(Icons.Rounded.ViewList)
    )

    object Settings : BottomBar(
        route = "SETTINGS",
        title = R.string.settings,
        icon = com.example.onehourapp.presentation.helpers.IconResource.fromImageVector(Icons.Rounded.Settings)
    )
}