package com.example.onehourapp.ui.screens.home.calendar

import android.util.Log
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.rememberMotionLayoutState
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.onehourapp.R
import com.example.onehourapp.ui.helpers.UITextHelper.generateLoremIpsumWords
import com.example.onehourapp.ui.helpers.pagerFanTransition
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.BackgroundSecondColor
import com.example.onehourapp.ui.theme.CalendarCurrentDayFont
import com.example.onehourapp.ui.theme.CalendarDayFont
import com.example.onehourapp.ui.theme.MainFont
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.example.onehourapp.utils.CalendarUtil
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.pie.PieChart
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Composable
fun CalendarScreenContent(navController: NavHostController, isChanged: MutableState<Boolean>) {
    //HorizontalPagerContent(navController, isChanged)
    BackdropScaffoldExample(isChanged)
}

data class ActivityRecordView(
    val day: Int,
    val hour: Int,
    val color: Color
)
@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
fun BackdropScaffoldExample(isChanged: MutableState<Boolean>) {
    val months = listOf(
        stringResource(R.string.january),
        stringResource(R.string.february),
        stringResource(R.string.march),
        stringResource(R.string.april),
        stringResource(R.string.may),
        stringResource(R.string.june),
        stringResource(R.string.july),
        stringResource(R.string.august),
        stringResource(R.string.september),
        stringResource(R.string.october),
        stringResource(R.string.november),
        stringResource(R.string.december)
    )
    val startIndex = Int.MAX_VALUE / 2 - 3 + CalendarUtil.getCurrentMonth()

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        initialPageOffsetFraction = 0f
    ) {
        return@rememberPagerState Int.MAX_VALUE
    }

    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val activityViewModel: ActivityViewModel = hiltViewModel()
    val activityRecordViewModel: ActivityRecordViewModel = hiltViewModel()
    val textMeasurer = rememberTextMeasurer()

    val year = remember {
        mutableIntStateOf(1000)
    }
    LaunchedEffect(Unit) {
        delay(200)
        year.value = CalendarUtil.getCurrentYear()
    }
    val loadedData = remember {
        mutableStateMapOf<Int, List<ActivityRecordView>>()
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    LaunchedEffect(scaffoldState) {
        scaffoldState.reveal()
    }
    BackdropScaffold(
        modifier = Modifier.background(BackgroundColor),
        backLayerBackgroundColor = BackgroundSecondColor,
        frontLayerScrimColor = Color.Transparent,
        frontLayerBackgroundColor = Color.Transparent,
        frontLayerElevation = 5.dp,
        persistentAppBar = true,
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                contentColor = Color.White,
                elevation = 0.dp,
                backgroundColor = Color.Transparent
            ){
                Row(horizontalArrangement = Arrangement.Center) {
                    Box(
                        Modifier
                            .padding(5.dp)
                            .fillMaxHeight()) {
                        if (scaffoldState.isRevealed) {
                            IconButton(onClick = { scope.launch { scaffoldState.conceal() } }) {
                                Icon(
                                    Icons.Filled.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        } else {
                            IconButton(onClick = { scope.launch { scaffoldState.reveal() } }) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }
                    }
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)){
                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            color = Color.Red,
                            text = months[pagerState.currentPage % 12],
                            style = MainFont.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                    Box(
                        Modifier
                            .padding(5.dp)
                            .fillMaxHeight()) {
                        IconButton(
                            onClick = {
                                scope.launch {

                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.MoreHoriz,
                                contentDescription = "DropDownMenu"
                            )
                        }
                    }
                }
            }
        },
        backLayerContent = {
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collect { page ->
                    updateLoadedData(
                        loadedData,
                        page,
                        activityRecordViewModel,
                        activityViewModel,
                        categoryViewModel
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(BackgroundColor),
                verticalAlignment = Alignment.CenterVertically,
                pageSpacing = 45.dp
            ){pageIndex->
                LaunchedEffect(key1 = Unit) {
                    //if(loadedData[pageIndex%12]==null)
                    updateLoadedData(
                        loadedData,
                        pageIndex,
                        activityRecordViewModel,
                        activityViewModel,
                        categoryViewModel
                    )
                }
                if (isChanged.value) {
                    LaunchedEffect(key1 = Unit) {
                        updateLoadedData(
                            loadedData,
                            pageIndex,
                            activityRecordViewModel,
                            activityViewModel,
                            categoryViewModel
                        )
                        isChanged.value = false
                    }
                }
                val item = loadedData[pageIndex % 12]
                if (item == null)
                    CircularProgressIndicator()
                else
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .pagerFanTransition(pageIndex, pagerState),
                ) {
                    val columns =
                        CalendarUtil.getDaysInMonth(
                            pageIndex % 12,
                            CalendarUtil.getCurrentYear()
                        )
                    val rows = 25
                    val spacing = 5.dp
                    val isCurrentMonth = pageIndex % 12 == CalendarUtil.getCurrentMonth()
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        drawRect(
                            color = BackgroundColor,
                            size = Size(size.width, size.height)
                        )
                        val cellSize = (size.width - 5 * (columns + 1)) / columns
                        for (row in 0 until rows) {
                            for (column in 0 until columns) {
                                val day = column + 1
                                val isCurrentDay =
                                    (day == CalendarUtil.getCurrentDay() && isCurrentMonth)
                                var x = column * (cellSize + 5)
                                var y = row * (cellSize + 5)
                                val center = (columns - 1) / 2
                                var offset = 0
                                when (column) {
                                    in 0..center - 2 -> offset = column
                                    in center - 2..center + 2 -> offset = center - 2
                                    in center + 2..columns -> offset = columns - column
                                }
                                if (row != 0) {
                                    val record = item.find {
                                        it.day == column + 1 && it.hour == row - 1
                                    }
                                    y += 5
                                    drawRoundRect(
                                        color = record?.color ?: Color.DarkGray,
                                        topLeft = Offset(
                                            x + spacing.toPx(),
                                            y - offset + spacing.toPx()
                                        ),
                                        size = Size(cellSize, cellSize),
                                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                                    )
                                } else {
                                    var textOffset = 0
                                    if ((column + 1) % 10 == 0)
                                        textOffset = 2
                                    if (column + 1 == columns)
                                        textOffset = 4
                                    drawText(
                                        style = if (isCurrentDay) CalendarCurrentDayFont else CalendarDayFont,
                                        text = "$day",
                                        textMeasurer = textMeasurer,
                                        topLeft = Offset(
                                            x + spacing.toPx() - textOffset,
                                            y - offset + spacing.toPx()
                                        ),
                                    )
                                }

                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(if (columns < 30) 40.dp else 0.dp))

                }
            }
    },
        frontLayerContent = {
        Box(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .clip(
                    RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp)
                )
                .background(BackgroundSecondColor)
        ) {
            var isLoaded by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = isLoaded){
                delay(500)
                isLoaded = true
            }
            val categories = categoryViewModel.getCategories().collectAsState(initial = emptyList())
            if(isLoaded)
            LazyColumn {
                item {
                    Text(
                        "Select",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                item {
                    Box(Modifier.wrapContentSize()) {
                        //TODO: обновить чарт при isChanged
                        categoryViewModel.sortType = CategoryViewModel.SortType.COLOR
                        val categories = categoryViewModel.getCategories().collectAsState(initial = emptyList()).value
                        val categoriesValues = categories.map{value -> activityRecordViewModel.getActivityRecordsCountByCategoryInMonth(value.id,year.value,pagerState.currentPage%12).toFloat()}
                        val totalHours = 24 * CalendarUtil.getDaysInMonth(pagerState.currentPage%12).toFloat()
                        val data = PieChartData(
                            entries = categoriesValues.mapIndexed { index, value ->
                                PieChartEntry(
                                    value = value,
                                    label = AnnotatedString(
                                        categories[index].name + String.format(" - %.1f", (value/totalHours*100f)) +"%"
                                    ),
                                    color = Color(categories[index].color.toColorInt())
                                )
                            }.plus(
                                PieChartEntry(
                                    value = totalHours - categoriesValues.sum(),
                                    label = AnnotatedString(
                                        "N/A"
                                    ),
                                    color = Color.Gray
                            )).filter { it.value > 0f },
                            colors = categories.filterIndexed{index, _-> categoriesValues[index]>0f}.map{category -> Color(category.color.toColorInt()) }.plus(Color.Gray),
                            legendPosition = LegendPosition.End,
                            legendShape = CircleShape,
                        )
                        PieChart(
                            data = data, chartSize = 140.dp, modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(32.dp)
                        )
                    }
                }
                items(categories.value.size) {
                    ListItem(
                        text = { Text(categories.value[it].name) },
                        icon = {
                            Icon(
                                Icons.Filled.Circle,
                                contentDescription = null,
                                tint = Color(categories.value[it].color.toColorInt())
                            )
                        }
                    )
                    val activities = activityViewModel.getActivities(categories.value[it].id).collectAsState(
                        initial = emptyList()
                    )
                    for(activity in activities.value){
                        ListItem(
                            text = { Text(activity.name) },
                            icon = {
                                Icon(
                                    modifier = Modifier.padding(5.dp).padding(start=10.dp),
                                    imageVector = Icons.Filled.Circle,
                                    contentDescription = null,
                                    tint = Color(categories.value[it].color.toColorInt())
                                )
                            }
                        )
                    }
                }
            }
        }
    })
}
suspend fun updateLoadedData(
    loadedData: SnapshotStateMap<Int, List<ActivityRecordView>>,
    pageIndex: Int,
    activityRecordViewModel: ActivityRecordViewModel,
    activityViewModel: ActivityViewModel,
    categoryViewModel: CategoryViewModel
) {
    loadedData[pageIndex % 12] = activityRecordViewModel.getActivityRecordsByMonth(
        CalendarUtil.getCurrentYear(),
        pageIndex % 12
    ).first().map { record->
        val day = CalendarUtil.getCurrentDay(record.timestamp)
        val hour = CalendarUtil.getCurrentHour(record.timestamp)
        val category = categoryViewModel.getCategoryById(
            activityViewModel.getActivityById(record.activityId).categoryId
        )
        ActivityRecordView(day, hour, Color(category.color.toColorInt()))
    }
}




