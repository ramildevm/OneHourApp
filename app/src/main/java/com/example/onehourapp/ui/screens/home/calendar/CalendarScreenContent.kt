package com.example.onehourapp.ui.screens.home.calendar

import android.graphics.Typeface
import android.text.TextUtils
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.onehourapp.R
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.data.models.Category
import com.example.onehourapp.ui.components.ClockProgressIndicator
import com.example.onehourapp.ui.helpers.pagerFanTransition
import com.example.onehourapp.ui.theme.ActivityListItemFont2
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.BackgroundSecondColor
import com.example.onehourapp.ui.theme.CalendarCurrentDayFont
import com.example.onehourapp.ui.theme.CalendarDayFont
import com.example.onehourapp.ui.theme.CardActivityColor
import com.example.onehourapp.ui.theme.CardCategoryColorSecond
import com.example.onehourapp.ui.theme.CategoryListItemFont2
import com.example.onehourapp.ui.theme.CategoryListItemFont2Inner
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.ui.theme.MainFont
import com.example.onehourapp.ui.theme.PieChartLabelFont
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.example.onehourapp.utils.CalendarUtil
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun CalendarScreenContent(navController: NavHostController, changedMonth: MutableState<Int>) {
    CalendarBackdropScaffold(changedMonth)
}

data class ActivityRecordView(
    val day: Int,
    val hour: Int,
    val color: Color
)

data class RecordCountCategory(
    val category: Category,
    val count: Int
)

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
fun CalendarBackdropScaffold(changedMonth: MutableState<Int>) {
    var isLoaded by remember {
        mutableStateOf(false)
    }
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

    var previousPageStart by remember {
        mutableIntStateOf((Int.MAX_VALUE / 2).toInt() - 3)
    }

    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val activityViewModel: ActivityViewModel = hiltViewModel()
    val activityRecordViewModel: ActivityRecordViewModel = hiltViewModel()
    val textMeasurer = rememberTextMeasurer()

    val year = remember {
        mutableIntStateOf(CalendarUtil.getCurrentYear())
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            delay(2000)
            isLoaded = true
        }
    }
    val loadedData = remember {
        mutableStateMapOf<Int, List<ActivityRecordView>>()
    }
    val loadedCategoriesCount = remember {
        mutableStateMapOf<Int, List<RecordCountCategory>>()
    }
    categoryViewModel.sortType = CategoryViewModel.SortType.COLOR

    val tabPagerState = com.google.accompanist.pager.rememberPagerState(0)

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    LaunchedEffect(scaffoldState) {
        scaffoldState.reveal()
    }
    BackdropScaffold(
        modifier = Modifier.background(BackgroundColor),
        backLayerBackgroundColor = BackgroundSecondColor,
        frontLayerScrimColor = Color.Transparent,
        frontLayerBackgroundColor = BackgroundSecondColor,
        frontLayerElevation = 5.dp,
        persistentAppBar = true,
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                contentColor = Color.White,
                elevation = 0.dp,
                backgroundColor = Color.Transparent
            ) {
                Row {
                    Box(
                        Modifier
                            .padding(5.dp)
                            .fillMaxHeight()
                    ) {
                        if (scaffoldState.isRevealed) {
                            IconButton(onClick = { scope.launch { scaffoldState.conceal() } }) {
                                Icon(
                                    Icons.Filled.ExpandLess,
                                    contentDescription = "Expand less"
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
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            color = Color.Red,
                            text = months[pagerState.currentPage % 12] + " ${year.intValue}",
                            style = MainFont.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Box(
                        Modifier
                            .padding(5.dp)
                            .fillMaxHeight()
                    ) {
                        var expanded by remember { mutableStateOf(false) }
                        IconButton(onClick = {
                            expanded = !expanded
                        }) {
                            Icon(
                                Icons.Filled.MoreHoriz,
                                contentDescription = "More"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                //TODO: Color canvas
                            }) {
                                Text(stringResource(id = R.string.edit))
                            }
                        }
                    }
                }
            }
        },
        backLayerContent = {
            LaunchedEffect(key1 = pagerState){
                snapshotFlow { pagerState.currentPage }.collect { page ->
                    when(page%12){
                        0 -> {
                            if (page - previousPageStart == 12) {
                                previousPageStart = page
                                year.intValue++
                                updateLoadedData(
                                    loadedData,
                                    year,
                                    12,
                                    activityRecordViewModel,
                                    activityViewModel,
                                    categoryViewModel
                                )
                                updateLoadedCategoriesCountList(
                                    loadedCategoriesCount,
                                    year,
                                    12,
                                    activityRecordViewModel,
                                    categoryViewModel
                                )
                            }
                        }
                        1->{
                            val previousYear = mutableIntStateOf(year.intValue-1)
                            updateLoadedData(
                                loadedData,
                                previousYear,
                                11,
                                activityRecordViewModel,
                                activityViewModel,
                                categoryViewModel
                            )
                            updateLoadedCategoriesCountList(
                                loadedCategoriesCount,
                                previousYear,
                                11,
                                activityRecordViewModel,
                                categoryViewModel
                            )
                        }
                        2->{
                            loadedData.remove(11)
                            loadedCategoriesCount.remove(11)
                        }
                        9->{
                            loadedData.remove(0)
                            loadedCategoriesCount.remove(0)
                        }
                        10->{
                            val nextYear = mutableIntStateOf(year.intValue+1)
                            updateLoadedData(
                                loadedData,
                                nextYear,
                                0,
                                activityRecordViewModel,
                                activityViewModel,
                                categoryViewModel
                            )
                            updateLoadedCategoriesCountList(
                                loadedCategoriesCount,
                                nextYear,
                                0,
                                activityRecordViewModel,
                                categoryViewModel
                            )
                        }
                        11->{
                            if (previousPageStart - page== 1) {
                                previousPageStart = page - 11
                                year.intValue--
                                updateLoadedData(
                                    loadedData,
                                    year,
                                    -1,
                                    activityRecordViewModel,
                                    activityViewModel,
                                    categoryViewModel
                                )
                                updateLoadedCategoriesCountList(
                                    loadedCategoriesCount,
                                    year,
                                    -1,
                                    activityRecordViewModel,
                                    categoryViewModel
                                )
                            }
                        }

                    }
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
            ) { pageIndex ->
                LaunchedEffect(key1 = Unit) {
                    if (loadedData[pageIndex % 12] == null)
                        updateLoadedData(
                            loadedData,
                            year,
                            pageIndex%12,
                            activityRecordViewModel,
                            activityViewModel,
                            categoryViewModel
                        )
                    if (loadedCategoriesCount[pageIndex % 12] == null) {
                        loadedCategoriesCount[pageIndex % 12] =
                            categoryViewModel.getCategories().first().map { category ->
                                val count =
                                    activityRecordViewModel.getActivityRecordsCountByCategoryInMonth(
                                        category.id,
                                        year.value,
                                        pageIndex % 12,
                                    )
                                RecordCountCategory(category, count)
                            }.filter { it.count > 0 }
                    }
                }
                if (changedMonth.value != -1) {
                    LaunchedEffect(key1 = Unit) {
                        updateLoadedData(
                            loadedData,
                            year,
                            changedMonth.value,
                            activityRecordViewModel,
                            activityViewModel,
                            categoryViewModel
                        )
                        updateLoadedCategoriesCountList(
                            loadedCategoriesCount,
                            year,
                            changedMonth.value,
                            activityRecordViewModel,
                            categoryViewModel
                        )
                        tabPagerState.animateScrollToPage(0)
                        changedMonth.value = -1
                    }

                }
                val item = loadedData[pageIndex % 12]
                if (item == null)
                    ClockProgressIndicator(item==null)
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
                                year.value
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
                                    val isCurrentDay = (day == CalendarUtil.getCurrentDay() && isCurrentMonth)
                                    val isCurrentHour = (day == CalendarUtil.getCurrentDay() && isCurrentMonth && row == CalendarUtil.getCurrentHour())
                                    val x = column * (cellSize + 5)
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
                                            cornerRadius = CornerRadius(
                                                2.dp.toPx(),
                                                2.dp.toPx()
                                            )
                                        )
                                        if(isCurrentHour)
                                            drawRoundRect(
                                                color = Color.White,
                                                topLeft = Offset(
                                                    x + spacing.toPx(),
                                                    y - offset + spacing.toPx()
                                                ),
                                                size = Size(cellSize, cellSize),
                                                cornerRadius = CornerRadius(
                                                    2.dp.toPx(),
                                                    2.dp.toPx()
                                                ),
                                                style = Stroke(width=1.dp.toPx())
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
                val titles = listOf(stringResource(R.string.general), stringResource(R.string.by_days))
                val month =  pagerState.currentPage % 12
                com.google.accompanist.pager.HorizontalPager(modifier = Modifier
                    .padding(top = 80.dp)
                    .fillMaxWidth(),
                    state = tabPagerState, count = titles.size) { page->
                    LazyColumn(
                        Modifier
                            .fillMaxHeight()
                            .align(Alignment.TopCenter),) {
                        when (page) {
                            0 -> {
                                val loadedCategories = loadedCategoriesCount[month]
                                if (loadedCategories != null) {
                                    item {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(
                                            Modifier
                                                .wrapContentSize()
                                                .background(BackgroundSecondColor)
                                        ) {
                                            val totalHours = 24f * CalendarUtil.getDaysInMonth(pagerState.currentPage % 12)
                                            val pieChartData = PieChartData(
                                                slices = loadedCategories.mapIndexed { _, value ->
                                                    PieChartData.Slice(
                                                        label =
                                                        value.category.name + String.format(
                                                            " - %.1f",
                                                            (value.count / totalHours * 100f)
                                                        ) + "%",
                                                        color = Color(value.category.color.toColorInt()),
                                                        value = value.count.toFloat()
                                                    )
                                                }.plus(
                                                    PieChartData.Slice(
                                                        value = totalHours - loadedCategories.sumOf { it.count },
                                                        label = "N/A",
                                                        color = Color.Gray
                                                    )
                                                ),
                                                plotType = PlotType.Donut
                                            )
                                            key(pieChartData.hashCode()) {
                                                val pieChartConfig =
                                                    PieChartConfig(
                                                        labelVisible = true,
                                                        strokeWidth = 100f,
                                                        labelColor = Color.White,
                                                        inActiveSliceAlpha = .8f,
                                                        isEllipsizeEnabled = true,
                                                        labelTypeface = Typeface.defaultFromStyle(
                                                            Typeface.NORMAL
                                                        ),
                                                        isAnimationEnable = false,
                                                        chartPadding = 40,
                                                        labelFontSize = 30.sp,
                                                        backgroundColor = BackgroundSecondColor,
                                                        sliceLabelEllipsizeAt = TextUtils.TruncateAt.MIDDLE
                                                    )
                                                Column(
                                                    Modifier
                                                        .height(450.dp)
                                                        .background(BackgroundSecondColor)
                                                ) {
                                                    val legendsList = mutableListOf<LegendLabel>()
                                                    pieChartData.slices.forEach { slice ->
                                                        legendsList.add(
                                                            LegendLabel(
                                                                slice.color,
                                                                slice.label
                                                            )
                                                        )
                                                    }
                                                    Legends(
                                                        legendsConfig = LegendsConfig(
                                                            legendLabelList = legendsList,
                                                            gridColumnCount = 3,
                                                            legendsArrangement = Arrangement.Start,
                                                            textStyle = PieChartLabelFont,
                                                            colorBoxSize = 15.dp,
                                                            textSize = 10.sp
                                                        )
                                                    )
                                                    DonutPieChart(
                                                        modifier = Modifier
                                                            .background(BackgroundSecondColor)
                                                            .fillMaxWidth()
                                                            .weight(1f),
                                                        pieChartData,
                                                        pieChartConfig
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    item {
                                        Column(verticalArrangement = Arrangement.Center) {
                                            val listFirstCategory =
                                                if (loadedCategories.isNotEmpty()) loadedCategories.first().category else null
                                            if (listFirstCategory != null) {
                                                var selectedCategory: Category by remember {
                                                    mutableStateOf(listFirstCategory)
                                                }
                                                var categoryListExpanded by remember {
                                                    mutableStateOf(false)
                                                }
                                                ExposedDropdownMenuBox(
                                                    expanded = categoryListExpanded,
                                                    onExpandedChange = {
                                                        categoryListExpanded = !categoryListExpanded
                                                    },
                                                    modifier = Modifier
                                                        .align(Alignment.CenterHorizontally)
                                                        .padding(horizontal = 10.dp)
                                                        .fillMaxWidth(),
                                                ) {
                                                    TextField(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        value = selectedCategory.name,
                                                        onValueChange = {},
                                                        readOnly = true,
                                                        leadingIcon = {
                                                            Icon(
                                                                imageVector = Icons.Default.Circle,
                                                                tint = Color(selectedCategory.color.toColorInt()),
                                                                contentDescription = null
                                                            )
                                                        },
                                                        trailingIcon = {
                                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                                expanded = categoryListExpanded
                                                            )
                                                        },
                                                    )
                                                    ExposedDropdownMenu(
                                                        expanded = categoryListExpanded,
                                                        onDismissRequest = {
                                                            categoryListExpanded = false
                                                        }
                                                    ) {
                                                        loadedCategories.map { it.category }
                                                            .forEach { category ->
                                                                DropdownMenuItem(
                                                                    content = {
                                                                        Row {
                                                                            Text(
                                                                                "● ",
                                                                                fontSize = 20.sp,
                                                                                color = Color(category.color.toColorInt())
                                                                            )
                                                                            Text(text = category.name)
                                                                        }
                                                                    },
                                                                    onClick = {
                                                                        selectedCategory = category
                                                                        categoryListExpanded = false
                                                                    }
                                                                )
                                                            }
                                                    }
                                                }

                                                val barDataList: ArrayList<BarData> =
                                                    getBarDataListByCategoryInMonth(
                                                        activityRecordViewModel,
                                                        selectedCategory,
                                                        year.value,
                                                        pagerState.currentPage % 12
                                                    )
                                                val ySteps = 25
                                                val xSteps = CalendarUtil.getDaysInMonth(
                                                    pagerState.currentPage % 12,
                                                    year.value
                                                ) + 2
                                                val hourString = stringResource(id = R.string.hours)
                                                val daysString = stringResource(id = R.string.days)

                                                val yAxisData = AxisData.Builder()
                                                    .backgroundColor(BackgroundSecondColor)
                                                    .axisLabelColor(Color.White)
                                                    .startPadding(20.dp)
                                                    .axisLineColor(Color.White)
                                                    .steps(ySteps)
                                                    .axisOffset(20.dp)
                                                    .axisLabelFontSize(10.sp)
                                                    .labelData { index ->
                                                        if (index > 24)
                                                            hourString
                                                        else
                                                            String.format(
                                                                "%4d",
                                                                index
                                                            ) //+ hourShortString
                                                    }
                                                    .build()

                                                val xAxisData = AxisData.Builder()
                                                    .backgroundColor(BackgroundSecondColor)
                                                    .axisLabelColor(Color.White)
                                                    .axisLineColor(Color.White)
                                                    .steps(xSteps)
                                                    .bottomPadding(20.dp)
                                                    .labelData { index ->
                                                        if (index != 0 && index != xSteps - 1)
                                                            index.toString()
                                                        else
                                                            ""
                                                    }
                                                    .build()


                                                val barChartData = BarChartData(
                                                    backgroundColor = BackgroundSecondColor,
                                                    chartData = barDataList,
                                                    xAxisData = xAxisData,
                                                    yAxisData = yAxisData,
                                                    barStyle = BarStyle(
                                                        20.dp,
                                                        paddingBetweenBars = 3.dp
                                                    ),
                                                )
                                                Box(
                                                    Modifier
                                                        .height(300.dp)
                                                        .background(BackgroundSecondColor)
                                                ) {
                                                    BarChart(
                                                        modifier = Modifier
                                                            .background(BackgroundSecondColor)
                                                            .fillMaxWidth()
                                                            .height(300.dp),
                                                        barChartData = barChartData
                                                    )
                                                    Box(
                                                        Modifier
                                                            .fillMaxSize()
                                                            .background(
                                                                brush = Brush.horizontalGradient(
                                                                    0f to Color.Transparent,
                                                                    0.9f to Color.Transparent,
                                                                    0.93f to BackgroundSecondColor,
                                                                    1f to BackgroundSecondColor
                                                                )
                                                            )
                                                    )
                                                    Text(
                                                        daysString,
                                                        fontSize = 14.sp,
                                                        modifier = Modifier
                                                            .padding(
                                                                horizontal = 18.dp,
                                                                vertical = 14.dp
                                                            )
                                                            .align(Alignment.BottomStart)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    itemsIndexed(loadedCategories.filter { it.count > 0 }) { _, category ->
                                        var expanded by remember {
                                            mutableStateOf(false)
                                        }
                                        Card(
                                            modifier = Modifier.padding(
                                                vertical = 4.dp,
                                                horizontal = 8.dp
                                            ),
                                            backgroundColor = CardCategoryColorSecond,
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            CategoryCardContent(
                                                category,
                                                year.value,
                                                pagerState.currentPage % 12,
                                                activityViewModel,
                                                activityRecordViewModel,
                                                expanded
                                            ) { expanded = !expanded }
                                        }

                                    }
                                }
                            }
                            1 -> {
                                val isCurrentMonth = month == CalendarUtil.getCurrentMonth()
                                val itemsCount = if(isCurrentMonth) CalendarUtil.getCurrentDay() else CalendarUtil.getDaysInMonth(month)
                                for(dayNumber in itemsCount downTo 1){
                                    val activityViewList = mutableListOf<Pair<Int, Int>>()
                                    for(time in 0..23) {
                                        val activityRecord =
                                            activityRecordViewModel.getActivityRecordByTime(
                                                year.value,
                                                month,
                                                dayNumber,
                                                time
                                            ) ?: ActivityRecord(0, 0, 0L)
                                        activityViewList.add(time to activityRecord.activityId)
                                    }
                                    val outputList = mutableListOf<Triple<Int, Int, Int>>()
                                    var currentId: Int? = null
                                    var count = 0
                                    var isListEmpty = true
                                    for ((time, id) in activityViewList) {
                                        if (id != currentId) {
                                            if(id!=0) isListEmpty = false
                                            if (currentId != null) {
                                                outputList.add(Triple(time-count, currentId, count))
                                            }
                                            currentId = id
                                            count = 1
                                        } else {
                                            count++
                                        }
                                    }
                                    if (currentId != null) {
                                        outputList.add(Triple(activityViewList.last().first-count+1, currentId, count))
                                    }
                                    item{
                                        val monthsFormatted = listOf(
                                            stringResource(R.string.january_date_format),
                                            stringResource(R.string.february_date_format),
                                            stringResource(R.string.march_date_format),
                                            stringResource(R.string.april_date_format),
                                            stringResource(R.string.may_date_format),
                                            stringResource(R.string.june_date_format),
                                            stringResource(R.string.july_date_format),
                                            stringResource(R.string.august_date_format),
                                            stringResource(R.string.september_date_format),
                                            stringResource(R.string.september_date_format),
                                            stringResource(R.string.november_date_format),
                                            stringResource(R.string.december_date_format)
                                        )
                                        if(!isListEmpty) {
                                            val dayText = if(isCurrentMonth && dayNumber == CalendarUtil.getCurrentDay())
                                                stringResource(id = R.string.today)
                                            else if(isCurrentMonth && dayNumber == CalendarUtil.getCurrentDay() - 1)
                                                stringResource(id = R.string.yesterday)
                                            else
                                                String.format(monthsFormatted[month], dayNumber)
                                            Text(
                                                modifier = Modifier
                                                    .padding(top = 10.dp, bottom = 4.dp)
                                                    .padding(
                                                        horizontal = 25.dp
                                                    ),
                                                fontSize = 16.sp,
                                                color = MainColorSecondRed,
                                                text = dayText,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                            Spacer(modifier = Modifier
                                                .padding(bottom = 6.dp)
                                                .padding(horizontal = 20.dp)
                                                .clip(
                                                    RoundedCornerShape(5.dp)
                                                )
                                                .background(
                                                    MainColorSecondRed
                                                )
                                                .height(3.dp)
                                                .fillMaxWidth())
                                        }
                                    }
                                    item {
                                        Column(Modifier.fillMaxWidth()) {
                                            for ((time, id, count) in outputList){
                                                if(id!=0) {
                                                    val activity = activityViewModel.getActivityById(id)
                                                    val category = categoryViewModel.getCategoryById(activity!!.categoryId)
                                                    Row(horizontalArrangement = Arrangement.Center){
                                                        Text(
                                                            modifier = Modifier
                                                                .padding(start = 10.dp)
                                                                .align(Alignment.CenterVertically),
                                                            text = "● ",
                                                            color = Color(category.color.toColorInt()),
                                                            fontSize = 25.sp
                                                        )
                                                        Text( modifier = Modifier
                                                            .padding(bottom = 3.dp, end = 10.dp)
                                                            .align(Alignment.CenterVertically)
                                                            .wrapContentHeight()
                                                            .weight(.75f), text= activity.name + ":", fontWeight = FontWeight.Medium)
                                                        Text(modifier = Modifier
                                                            .align(Alignment.CenterVertically)
                                                            .alpha(.75f),text = String.format("%02d:00 - %02d:00"
                                                            , time, time + count ), fontWeight = FontWeight.Bold
                                                        )
                                                        Text(fontWeight = FontWeight.Bold,
                                                            modifier = Modifier
                                                                .align(Alignment.CenterVertically)
                                                                .padding(
                                                                    horizontal = 4.dp
                                                                )
                                                                .weight(.25f)
                                                                .padding(end = 6.dp), textAlign = TextAlign.End, text = "$count " + stringResource(id = R.string.hour_short))
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }
                        }
                    }

                }
                Column(Modifier.height(80.dp)) {
                    Text(
                        text = stringResource(id = R.string.information),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .align(Alignment.CenterHorizontally)
                            .background(BackgroundSecondColor),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    TabRow(
                        indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(
                                tabPagerState,
                                tabPositions
                            )
                        )
                        },
                        modifier = Modifier.weight(1f),
                        selectedTabIndex = tabPagerState.currentPage,
                        backgroundColor = BackgroundSecondColor
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title) },
                                selected = tabPagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        tabPagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        })
    if (!isLoaded)
        Box(
            Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            ClockProgressIndicator(isLoaded)
        }
}

@Composable
fun CategoryCardContent(
    category: RecordCountCategory,
    year: Int,
    month: Int,
    activityViewModel: ActivityViewModel,
    activityRecordViewModel: ActivityRecordViewModel,
    expanded: Boolean,
    onExpand: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable {
                onExpand()
            }
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Row {
            Icon(
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp),
                imageVector = Icons.Rounded.Circle,
                contentDescription = "Icon",
                tint = Color(category.category.color.toColorInt())
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(
                    text = category.category.name,
                    modifier = Modifier
                        .weight(1f),
                    style = CategoryListItemFont2
                )
                val categoryHourCount = category.count
                Text(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterVertically),
                    text = categoryHourCount.toString() + " " + stringResource(id = R.string.hour_short),
                    style = CategoryListItemFont2Inner
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { onExpand() }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Expand button"
                )
            }
        }
        if (expanded) {
            val activities =
                activityViewModel.getActivities(category.category.id)
                    .collectAsState(initial = emptyList())
            Column(modifier = Modifier.background(CardCategoryColorSecond)) {
                activities.value.forEach { activity ->
                    key(activity.hashCode()) {
                        Card(
                            elevation = 4.dp,
                            modifier = Modifier
                                .background(CardCategoryColorSecond)
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(CardActivityColor)
                                    .padding(horizontal = 6.dp, 4.dp)
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .padding(vertical = 10.dp),
                                    imageVector = Icons.Rounded.Circle,
                                    contentDescription = "Icon",
                                    tint = Color(category.category.color.toColorInt())
                                )
                                Text(
                                    text = activity.name,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .weight(1f),
                                    style = ActivityListItemFont2
                                )
                                val activityHourCount =
                                    activityRecordViewModel.getActivityRecordsCountByActivityInMonth(
                                        activity.id,
                                        year,
                                        month
                                    )
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .align(Alignment.CenterVertically),
                                    text = activityHourCount.toString() + " " + stringResource(id = R.string.hour_short),
                                    style = CategoryListItemFont2Inner
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getBarDataListByCategoryInMonth(
    activityRecordViewModel: ActivityRecordViewModel,
    category: Category?,
    year: Int,
    month: Int
): ArrayList<BarData> {
    val list = arrayListOf<BarData>()
    val maxX = CalendarUtil.getDaysInMonth(month, year)
    list.add(BarData(Point(0f, 0f)))
    if (category != null)
        for (index in 1..maxX) {
            val count = activityRecordViewModel.getActivityRecordsCountByCategoryInDay(
                category.id,
                year,
                month,
                index
            )
            val point = Point(
                x = index.toFloat(),
                y = count.toFloat()
            )
            list.add(
                BarData(
                    point = point,
                    color = Color(category.color.toColorInt()),
                    label = "$index",
                )
            )
        }
    list.add(BarData(Point(maxX + 1f, 0f)))
    return list
}

suspend fun updateLoadedCategoriesCountList(
    loadedCategoriesCount: SnapshotStateMap<Int, List<RecordCountCategory>>,
    year: MutableIntState,
    month: Int,
    activityRecordViewModel: ActivityRecordViewModel,
    categoryViewModel: CategoryViewModel
) {
    val startMonth = if(month == 12) 1 else if( month==-1) 10 else month
    val endMonth = if(month == 12) 10 else if( month==-1) 1 else month
    val monthRange = if(month==12) (startMonth..endMonth) else (startMonth downTo endMonth)
    for(idx in monthRange) {
        loadedCategoriesCount[idx] =
            categoryViewModel.getCategories().first().toList().map { category ->
                val count = activityRecordViewModel.getActivityRecordsCountByCategoryInMonth(
                    category.id,
                    year.intValue,
                    idx,
                )
                RecordCountCategory(category, count)
            }.filter { it.count > 0 }
    }
}

suspend fun updateLoadedData(
    loadedData: SnapshotStateMap<Int, List<ActivityRecordView>>,
    year: MutableIntState,
    month: Int,
    activityRecordViewModel: ActivityRecordViewModel,
    activityViewModel: ActivityViewModel,
    categoryViewModel: CategoryViewModel
) {
    val startMonth = if(month == 12) 1 else if( month==-1) 10 else month
    val endMonth = if(month == 12) 10 else if( month==-1) 1 else month
    val monthRange = if(month==12) (startMonth..endMonth) else (startMonth downTo endMonth)
    for(idx in monthRange) {
        loadedData[idx] = activityRecordViewModel.getActivityRecordsByMonth(
            year.intValue,
            idx
        ).first().map { record ->
            val day = CalendarUtil.getCurrentDay(record.timestamp)
            val hour = CalendarUtil.getCurrentHour(record.timestamp)
            val category = categoryViewModel.getCategoryById(
                activityViewModel.getActivityById(record.activityId)!!.categoryId
            )
            ActivityRecordView(day, hour, Color(category.color.toColorInt()))
        }
    }

}




