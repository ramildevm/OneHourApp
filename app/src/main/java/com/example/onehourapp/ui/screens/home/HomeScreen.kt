package com.example.onehourapp.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.onehourapp.R
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.Category
import com.example.onehourapp.ui.graphs.HomeNavGraph
import com.example.onehourapp.ui.helpers.times
import com.example.onehourapp.ui.helpers.toHexString
import com.example.onehourapp.ui.helpers.transform
import com.example.onehourapp.ui.screens.home.activity.ColorButton
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.BottomBarAddColor
import com.example.onehourapp.ui.theme.BottomBarColor
import com.example.onehourapp.ui.theme.BottomBarLabelFontEn
import com.example.onehourapp.ui.theme.BottomBarLabelFontRu
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.ui.theme.TextFieldStyle
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.example.onehourapp.ui.viewmodels.UserSettingsViewModel
import com.example.onehourapp.utils.CalendarUtil
import kotlin.math.PI
import kotlin.math.sin


enum class AddButtonType{
    CATEGORY,
    ACTIVITY,
    RECORD,
    NONE
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    val addButtonClickType = remember { mutableStateOf(AddButtonType.NONE) }
    val isChanged = remember { mutableIntStateOf(-1) }
    val isAddMenuExtended = remember { mutableStateOf(false) }
    val categoryVM: CategoryViewModel = hiltViewModel()
    val activityVM: ActivityViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current
    val onDismiss: () -> Unit = {
        addButtonClickType.value = AddButtonType.NONE
        isAddMenuExtended.value = false
    }
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {innerPadding->
        when(addButtonClickType.value){
            AddButtonType.CATEGORY -> {
                val categories = categoryVM.getCategories().collectAsState(
                    initial = emptyList()
                )
                AddCategoryDialog(
                    onConfirm = { name: String, color: Color ->
                        categoryVM.insertCategory(Category(0, name, color.toHexString()))
                        categoryVM.insertResult.observe(lifecycleOwner) { result ->
                            activityVM.insertActivity(Activity(0, name, result))
                        }
                        onDismiss()
                    },
                    onDismiss = {
                        onDismiss()
                    },
                    categories = categories.value
                )
            }
            AddButtonType.ACTIVITY ->{
                val categories = categoryVM.getCategories().collectAsState(
                    initial = emptyList()
                )
                if(categories.value.isNotEmpty())
                    AddActivityDialog(
                        onDismiss = {
                            onDismiss()
                                    },
                        onConfirm = {name:String, categoryId:Int->
                            activityVM.insertActivity(Activity(0,name,categoryId))
                            onDismiss()
                        },
                        categories = categories,
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
                BottomBarLabelFontRu
            } else {
                BottomBarLabelFontEn
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
        selectedContentColor = if(isAddScreen) BottomBarAddColor else MainColorSecondRed,
        unselectedContentColor = if(isAddScreen) BottomBarAddColor else Color.White,
        onClick = {
            if(!isAddScreen)
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

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Color) -> Unit,
    categories:List<Category>
) {
    Dialog(onDismissRequest = onDismiss ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .shadow(
                    30.dp,
                    RoundedCornerShape(16.dp),
                    ambientColor = Color.White,
                    spotColor = Color.Gray
                ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(BackgroundColor)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                val colors = listOf(
                    Color(0xFFFF0000), Color(0xFFF84D2B), Color(0xFFFF8F2C),
                    Color(0xFFECFF00), Color(0xFF8FFF00), Color(0xFF01A92D),
                    Color(0xFF005B18), Color(0xFF00FF85), Color(0xFF6CFFC1),
                    Color(0xFF9DD1FF), Color(0xFF4784ED), Color(0xFF092F79),
                    Color(0xFF082152), Color(0xFF2A0084), Color(0xFF590070),
                    Color(0xFFB100B5), Color(0xFFFF9BFC), Color(0xFFFF00B8)
                )

                val usedColors =  categories.map {  Color(it.color.toColorInt()) }

                var selectedColor by remember { mutableStateOf<Color?>(null) }
                var text by rememberSaveable { mutableStateOf("") }

                Box(
                    Modifier
                        .padding(5.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .background(BackgroundColor)) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(BackgroundColor),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.add_category),
                            style = TextFieldStyle.copy(fontWeight = FontWeight.Bold, color = MainColorSecondRed),
                            modifier = Modifier.padding(20.dp, 4.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(id = R.string.input_name),
                            style = TextFieldStyle,
                            modifier = Modifier.padding(10.dp, 0.dp)
                        )
                        var isTextFieldEmpty by rememberSaveable { mutableStateOf(false) }

                        fun validate(text: String) {
                            isTextFieldEmpty = text.isEmpty()
                        }
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(10.dp, 4.dp)
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            value = text,
                            isError = isTextFieldEmpty,
                            textStyle = TextFieldStyle,
                            onValueChange = { newValue ->
                                if(newValue.length <= 50)
                                    text = newValue
                                validate(text)
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = MainColorSecondRed, cursorColor = MainColorSecondRed)
                        )
                        if(isTextFieldEmpty){
                            Text(
                                text = stringResource(R.string.empty_field),
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(18.dp)
                            )
                        }
                        else
                            Spacer(Modifier.height(18.dp))
                        Text(
                            text = stringResource(R.string.select_a_color),
                            style = TextFieldStyle,
                            modifier = Modifier.padding(10.dp, 0.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        var isColorNotSelected by rememberSaveable { mutableStateOf(false) }
                        repeat(4) { rowIndex ->
                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(vertical = 1.dp)
                                    .wrapContentWidth(),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(5) { columnIndex ->
                                    val index = rowIndex * 5 + columnIndex
                                    val color = colors.getOrNull(index)
                                    var used = false
                                    if (usedColors.find { it == color } != null)
                                        used = true

                                    if (color != null) {
                                        ColorButton(
                                            color = color,
                                            isUsed = used,
                                            isSelected = selectedColor == color,
                                            alpha = if (used) 0.5f else 1f,
                                            onClick = {
                                                if (used)
                                                    return@ColorButton
                                                if (selectedColor == color) {
                                                    selectedColor = null
                                                } else {
                                                    isColorNotSelected = false
                                                    selectedColor = color
                                                }
                                            }
                                        )
                                    }
                                    else
                                        Spacer(modifier = Modifier.size(44.dp))
                                }

                            }
                        }
                        if(isColorNotSelected){
                            Text(
                                text = stringResource(R.string.color_is_not_selected),
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(18.dp)
                            )
                        }
                        else
                            Spacer(Modifier.height(18.dp))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)) {
                            TextButton(onClick = {
                                if(text.isNotEmpty() && selectedColor!=null){
                                    onConfirm(text.trim(), selectedColor!!)
                                }
                                if(text.isEmpty()){
                                    isTextFieldEmpty = true
                                }
                                if(selectedColor==null ){
                                    isColorNotSelected = true
                                }},
                                Modifier.align(Alignment.CenterStart)) {
                                Text(stringResource(id = R.string.add), fontSize = 16.sp, color = MainColorSecondRed)
                            }
                            TextButton(onClick = onDismiss, Modifier.align(Alignment.CenterEnd)) {
                                Text(stringResource(id = R.string.cancel), fontSize = 16.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddActivityDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit,
    categories: State<List<Category>>,
    category: Category
) {
    Dialog(onDismissRequest = onDismiss ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .shadow(
                    30.dp,
                    RoundedCornerShape(16.dp),
                    ambientColor = Color.White,
                    spotColor = Color.Gray
                ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(BackgroundColor)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                var selectedCategory by remember { mutableStateOf(category) }
                var text by rememberSaveable { mutableStateOf("") }
                var expanded by remember { mutableStateOf(false) }

                Box(
                    Modifier
                        .padding(5.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .background(BackgroundColor)) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(BackgroundColor),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.add_activity),
                            style = TextFieldStyle.copy(fontWeight = FontWeight.Bold, color = MainColorSecondRed),
                            modifier = Modifier.padding(20.dp, 4.dp)
                        )
                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = stringResource(R.string.category)+":",
                            style = TextFieldStyle,
                            modifier = Modifier.padding(14.dp, 0.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}, modifier = Modifier.padding(horizontal = 10.dp)) {
                            TextField(
                                value = selectedCategory.name,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                leadingIcon = {Icon(Icons.Default.Circle ,null, tint=  Color(selectedCategory.color.toColorInt()))}
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categories.value.forEach { item ->
                                    DropdownMenuItem(
                                        content = {
                                            Row {
                                                Text(
                                                    "● ",
                                                    fontSize = 20.sp,
                                                    color = Color(item.color.toColorInt())
                                                )
                                                Text(text = item.name, Modifier.weight(1f))
                                            }
                                        },
                                        onClick = {
                                            selectedCategory = item
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Text(
                            text = stringResource(id = R.string.input_name),
                            style = TextFieldStyle,
                            modifier = Modifier.padding(14.dp, 0.dp)
                        )

                        var isTextFieldEmpty by rememberSaveable { mutableStateOf(false) }
                        fun validate(text: String) {
                            isTextFieldEmpty = text.isEmpty()
                        }
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(10.dp, 4.dp)
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            value = text,
                            isError = isTextFieldEmpty,
                            textStyle = TextFieldStyle,
                            onValueChange = { newValue ->
                                if(newValue.length <= 50)
                                    text = newValue
                                validate(text)
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = MainColorSecondRed, cursorColor = MainColorSecondRed)
                        )
                        if(isTextFieldEmpty){
                            Text(
                                text = stringResource(R.string.empty_field),
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(18.dp)
                            )
                        }
                        else
                            Spacer(Modifier.height(16.dp))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)) {
                            TextButton(onClick = {
                                if(text.isNotEmpty()){
                                    onConfirm(text, selectedCategory.id)
                                }
                                if(text.isEmpty()){
                                    isTextFieldEmpty = true
                                }},
                                Modifier.align(Alignment.CenterStart)) {
                                Text(stringResource(id = R.string.add), fontSize = 16.sp, color = MainColorSecondRed)
                            }
                            TextButton(onClick = onDismiss, Modifier.align(Alignment.CenterEnd)) {
                                Text(stringResource(id = R.string.cancel), fontSize = 16.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

