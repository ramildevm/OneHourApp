package com.example.onehourapp.ui.screens.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.onehourapp.ui.graphs.HomeNavGraph
import com.example.onehourapp.ui.screens.AddButtonScreen
import com.example.onehourapp.ui.screens.BottomBarScreen
import com.example.onehourapp.ui.theme.BottomBarAddColor
import com.example.onehourapp.ui.theme.BottomBarColor
import com.example.onehourapp.ui.theme.BottomBarLabelFontEn
import com.example.onehourapp.ui.theme.BottomBarLabelFontRu
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.utils.CalendarUtil
import java.util.Calendar


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    var isAddBtnClicked by remember { mutableStateOf(false) }
    var isChanged = remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = { BottomBar(navController = navController) { isAddBtnClicked = true } }
    ) {innerPadding->
        if(isAddBtnClicked)
            AddRecordDialog (
                date = Calendar.getInstance().timeInMillis,
                hour = CalendarUtil.getCurrentHour(),
                onDismiss = {isAddBtnClicked = false},
                notifyChange = {isChanged.value = true}
            )
        Box(modifier = Modifier.padding(innerPadding)){
            HomeNavGraph(navController = navController, isChanged = isChanged)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, onClick: () -> Unit, ) {
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
                    navController = navController,
                    onAddBtnClick = onClick
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
    navController: NavHostController,
    onAddBtnClick: () -> Unit
)
 {
     val isAddScreen = screen is AddButtonScreen

    BottomNavigationItem(
        label = {
            var fontStyle = BottomBarLabelFontEn
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val currentLocale = LocalConfiguration.current.locales[0]
                fontStyle = if (currentLocale.language == "ru") {
                    BottomBarLabelFontRu
                } else {
                    BottomBarLabelFontEn
                }
            }
            if(!isAddScreen)
                Text(text = stringResource(id = screen.title), style = fontStyle,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = (0).dp))
            else
                Spacer(modifier = Modifier.height(0.dp))
        },
        icon = {
            val modifier = Modifier.size(30.dp)
            val addScreenModifier = Modifier
                .offset(y = 5.dp)
                .fillMaxSize()
                .align(Alignment.Bottom)
            if(isAddScreen)
            Icon (modifier = addScreenModifier ,
                imageVector = Icons.Rounded.Circle,
                contentDescription =null,
                tint = Color.White )
            Icon(
                modifier = if(isAddScreen) addScreenModifier else modifier,
                painter = screen.icon.asPainterResource(),
                contentDescription = "Navigation Icon",
            )

        },
        alwaysShowLabel = !isAddScreen,
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        selectedContentColor = if(isAddScreen) BottomBarAddColor else MainColorSecondRed,
        unselectedContentColor = if(isAddScreen) BottomBarAddColor else Color.White,
        onClick = {
            if(isAddScreen)
                onAddBtnClick()
            else
            navController.navigate(screen.route) {
                navController.graph.startDestinationRoute?.let { screen_route ->
                    popUpTo(screen_route) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

