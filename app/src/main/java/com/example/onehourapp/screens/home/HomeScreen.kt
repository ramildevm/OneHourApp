package com.example.onehourapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.onehourapp.graphs.HomeNavGraph
import com.example.onehourapp.screens.AddButtonScreen
import com.example.onehourapp.screens.BottomBarScreen
import com.example.onehourapp.ui.theme.BottomBarAddColor
import com.example.onehourapp.ui.theme.BottomBarColor
import com.example.onehourapp.ui.theme.MainColorSecondRed

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        HomeNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.YearStat,
        BottomBarScreen.Calendar,
        BottomBarScreen.Add,
        BottomBarScreen.Activity,
        BottomBarScreen.Settings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomNavigation(
            backgroundColor = BottomBarColor,
            contentColor = Color.White,
            modifier = Modifier.height(60.dp)
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController)
 {
     val isAddScreen = screen is AddButtonScreen
    BottomNavigationItem(
        label = {
            if(!isAddScreen)
                Text(text = screen.title, fontSize = 9.sp)
            else
                Spacer(modifier = Modifier.height(0.dp))
        },
        icon = {
            val modifier = Modifier.size(30.dp)
            val addScreenModifier = Modifier.fillMaxSize().align(Alignment.Bottom).offset(0.dp,5.dp)
            if(isAddScreen)
            Icon (modifier = addScreenModifier ,
                imageVector = Icons.Rounded.Circle,
                contentDescription =null,
                tint = MainColorSecondRed )
            Icon(
                modifier = if(isAddScreen) addScreenModifier else modifier,
                painter = screen.icon.asPainterResource(),
                contentDescription = "Navigation Icon",
            )

        },
        alwaysShowLabel = true,
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        selectedContentColor = if(isAddScreen) BottomBarAddColor else MainColorSecondRed,
        unselectedContentColor = if(isAddScreen) BottomBarAddColor else Color.White,
        onClick = {
            if(isAddScreen)

            else
            navController.navigate(screen.route) {
                navController.graph.startDestinationRoute?.let { screen_route ->
                    popUpTo(screen_route) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            } //TODO: double click
        }
    )
}

