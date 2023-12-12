package com.example.onehourapp.presentation.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.onehourapp.R
import com.example.onehourapp.presentation.navigation.HomeNavGraph
import com.example.onehourapp.presentation.helpers.times
import com.example.onehourapp.dialog.activity.presentation.AddActivityDialog
import com.example.onehourapp.dialog.record.presentation.AddRecordDialog
import com.example.onehourapp.dialog.category.presentation.AddCategoryDialog
import com.example.onehourapp.presentation.helpers.transform
import kotlin.math.PI
import kotlin.math.sin


enum class AddButtonType{
    CATEGORY,
    ACTIVITY,
    RECORD,
    NONE
}
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    val addButtonClickType = remember { mutableStateOf(AddButtonType.NONE) }
    val isChanged = remember { mutableIntStateOf(-1) }
    val isAddMenuExtended = remember { mutableStateOf(false) }
    val onDismiss: () -> Unit = {
        addButtonClickType.value = AddButtonType.NONE
        isAddMenuExtended.value = false
    }
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {innerPadding->
        when(addButtonClickType.value){
            AddButtonType.CATEGORY -> {
                AddCategoryDialog(
                    onDismiss = {
                        onDismiss()
                    }
                )
            }
            AddButtonType.ACTIVITY ->{
                val categories = viewModel.categories.collectAsState(initial = emptyList())
                if(categories.value.isNotEmpty())
                    AddActivityDialog(
                        onDismiss = {
                            onDismiss()
                                    },
                        category = categories.value[0]
                    )
            }
            AddButtonType.RECORD ->
                AddRecordDialog (
                    onDismiss = {onDismiss()},
                    notifyChange = {month:Int -> isChanged.intValue = month}
                )
            AddButtonType.NONE -> {}
        }
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeNavGraph(navController = navController, isChanged = isChanged)
        }
    }
    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isAddMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = LinearEasing
        )
    )
    val clickAnimationProgress by animateFloatAsState(
        targetValue = if (isAddMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearEasing
        )
    )
    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Circle(
            color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            animationProgress = 0.5f
        )
        FabGroup(animationProgress = fabAnimationProgress, addButtonClickType)
        FabGroup(
            animationProgress = fabAnimationProgress,
            addButtonClickType
        ) { isAddMenuExtended.value = isAddMenuExtended.value.not() }
        Circle(
            color = Color.White,
            animationProgress = clickAnimationProgress
        )
    }
}

@Composable
fun Circle(color: Color, animationProgress: Float) {
    val animationValue = sin(PI * animationProgress).toFloat()

    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(56.dp)
            .scale(2 - animationValue)
            .border(
                width = 2.dp,
                color = color.copy(alpha = color.alpha * animationValue),
                shape = CircleShape
            )
    )
}

@Composable
fun FabGroup(
    animationProgress: Float = 0f,
    addButtonClickType: MutableState<AddButtonType>,
    toggleAnimation: () -> Unit = { },
) {
    Box(
        Modifier
            .fillMaxHeight()
            .graphicsLayer { this.renderEffect = renderEffect }
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        AnimatedFab(
            icon = Icons.Default.Category,
            text = stringResource(id = R.string.category),
            modifier = Modifier
                .padding(
                    PaddingValues(
                        bottom = 72.dp,
                        end = 200.dp
                    ) * FastOutSlowInEasing.transform(0f, 0.8f, animationProgress)
                ),
            opacity = LinearEasing.transform(0.2f, 0.7f, animationProgress)
        ){
            addButtonClickType.value = AddButtonType.CATEGORY
        }


        AnimatedFab(
            icon = Icons.Default.DirectionsRun,
            text = stringResource(id = R.string.activity),
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 88.dp,
                ) * FastOutSlowInEasing.transform(0.1f, 0.9f, animationProgress)
            ),
            opacity = LinearEasing.transform(0.3f, 0.8f, animationProgress)
        ){
            addButtonClickType.value = AddButtonType.ACTIVITY
        }

        AnimatedFab(
            icon = Icons.Default.EditNote,
            text = stringResource(R.string.record),
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 72.dp,
                    start = 200.dp
                ) * FastOutSlowInEasing.transform(0.2f, 1.0f, animationProgress)
            ),
            opacity = LinearEasing.transform(0.4f, 0.9f, animationProgress)
        ){
            addButtonClickType.value = AddButtonType.RECORD
        }

        AnimatedFab(
            modifier = Modifier
                .scale(1f - LinearEasing.transform(0.5f, 0.85f, animationProgress)),
        )

        AnimatedFab(
            icon = Icons.Default.Add,
            modifier = Modifier
                .rotate(
                    225 * FastOutSlowInEasing
                        .transform(0.35f, 0.65f, animationProgress)
                ),
            onClick = toggleAnimation,
            backgroundColor = Color.Transparent
        )
    }
}

@Composable
fun AnimatedFab(
    modifier: Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    opacity: Float = 1f,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    onClick: () -> Unit = {}
) {
    val localDensity = LocalDensity.current
    var buttonHeightDp by remember {
        mutableStateOf(0.dp)
    }
    FloatingActionButton(
        onClick = onClick,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        backgroundColor = backgroundColor,
        modifier = modifier
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    buttonHeightDp = with(localDensity) { (coordinates.size.height /2).toDp() }
                },
                imageVector = it,
                contentDescription = null,
                tint = Color.White.copy(alpha = opacity)
            )
        }
    }
    text?.let {
        Text(text=text, modifier=modifier.offset(y = buttonHeightDp + 7.dp), color = Color.Black.copy(alpha=opacity), style = MaterialTheme.typography.h4.copy(shadow = Shadow(
            color = Color.Black.copy(opacity),
            offset = Offset(0f, 4f),
            blurRadius = 8f
        )),fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(text, modifier.offset(y = buttonHeightDp + 7.dp), color = Color.White.copy(alpha=opacity), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBar.YearStat,
        BottomBar.Calendar,
        BottomBar.Add,
        BottomBar.Activity,
        BottomBar.Settings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomNavigation(
            backgroundColor = com.example.onehourapp.theme.ui.BottomBarColor,
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
    screen: BottomBar,
    currentDestination: NavDestination?,
    navController: NavHostController
)
 {
     val isAddScreen = screen.route == "ADD"
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true
    BottomNavigationItem(
        label = {
            val fontStyle: TextStyle
            val currentLocale = LocalConfiguration.current.locales[0]
            fontStyle = if (currentLocale.language == "ru") {
                com.example.onehourapp.theme.ui.BottomBarLabelFontRu
            } else {
                com.example.onehourapp.theme.ui.BottomBarLabelFontEn
            }
            if(!isAddScreen)
                Text(text = stringResource(id = screen.title), style = fontStyle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = (0).dp))
            else
                Spacer(modifier = Modifier.height(0.dp))
        },
        icon = {
            val modifier = Modifier.size(30.dp)
            if(!isAddScreen)
            Icon(
                modifier = modifier,
                painter = screen.icon.asPainterResource(),
                contentDescription = "Navigation Icon",
            )

        },
        alwaysShowLabel = false,
        selected = isSelected,
        selectedContentColor = if(isAddScreen) com.example.onehourapp.theme.ui.BottomBarAddColor else com.example.onehourapp.theme.ui.MainColorSecondRed,
        unselectedContentColor = if(isAddScreen) com.example.onehourapp.theme.ui.BottomBarAddColor else Color.White,
        onClick = {
            if(!isAddScreen)
                navController.navigate(screen.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
        }
    )
}

