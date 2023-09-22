package com.example.onehourapp.ui.screens.home.calendar

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.example.onehourapp.R
import com.example.onehourapp.data.models.Category
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
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.example.onehourapp.utils.CalendarUtil
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.pie.PieChart
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry
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
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
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

    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val activityViewModel: ActivityViewModel = hiltViewModel()
    val activityRecordViewModel: ActivityRecordViewModel = hiltViewModel()
    val textMeasurer = rememberTextMeasurer()

    var year by remember {
        mutableIntStateOf(1000)
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            delay(2000)
            isLoaded = true
        }
    }
    LaunchedEffect(Unit) {
        delay(200)
        year = CalendarUtil.getCurrentYear()
    }
    val loadedData = remember {
        mutableStateMapOf<Int, List<ActivityRecordView>>()
    }
    var loadedCategoriesCount = remember {
        mutableStateMapOf<Int, List<RecordCountCategory>>()
    }
    categoryViewModel.sortType = CategoryViewModel.SortType.COLOR

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
                Row(horizontalArrangement = Arrangement.Center) {
                    Box(
                        Modifier
                            .padding(5.dp)
                            .fillMaxHeight()
                    ) {
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
                            text = months[pagerState.currentPage % 12],
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
                            pageIndex,
                            activityRecordViewModel,
                            activityViewModel,
                            categoryViewModel
                        )
                    if(loadedCategoriesCount[pageIndex%12]==null){
                        loadedCategoriesCount[pageIndex%12] = categoryViewModel.getCategories().first().map {category->
                            val count = activityRecordViewModel.getActivityRecordsCountByCategoryInMonth(
                                category.id,
                                year,
                                pageIndex%12,
                            )
                            RecordCountCategory(category, count)
                        }.filter { it.count>0 }
                    }
                }
                if (changedMonth.value != -1) {
                    LaunchedEffect(key1 = Unit) {
                        updateLoadedData(
                            loadedData,
                            changedMonth.value,
                            activityRecordViewModel,
                            activityViewModel,
                            categoryViewModel
                        )
                        updateLoadedCategoriesCountList(loadedCategoriesCount,year, changedMonth.value,activityRecordViewModel,categoryViewModel)
                        changedMonth.value = -1
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
                                            cornerRadius = CornerRadius(
                                                2.dp.toPx(),
                                                2.dp.toPx()
                                            )
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
                val loadedCategories = loadedCategoriesCount[pagerState.currentPage%12]
                LazyColumn(Modifier.padding(top = 30.dp)) {
                    if(loadedCategories != null) {
                        item {
                            Box(Modifier.wrapContentSize()) {
                                val totalHours =
                                    24f * CalendarUtil.getDaysInMonth(pagerState.currentPage % 12)
                                val pieChartData = PieChartData(
                                    entries = loadedCategories.mapIndexed { _, value ->
                                        PieChartEntry(
                                            value = value.count.toFloat(),
                                            label = AnnotatedString(
                                                value.category.name + String.format(
                                                    " - %.1f",
                                                    (value.count / totalHours * 100f)
                                                ) + "%"
                                            ),
                                            color = Color(value.category.color.toColorInt())
                                        )
                                    }.plus(
                                        PieChartEntry(
                                            value = totalHours - loadedCategories.sumOf { it.count },
                                            label = AnnotatedString(
                                                "N/A"
                                            ),
                                            color = Color.Gray
                                        )
                                    ).filter { it.value > 0f },
                                    colors = loadedCategories
                                        .map { value -> Color(value.category.color.toColorInt()) }
                                        .plus(Color.Gray),
                                    legendPosition = LegendPosition.End,
                                    legendShape = CircleShape,
                                )

                                PieChart(
                                    data = pieChartData,
                                    chartSize = 140.dp,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(32.dp)
                                )
                            }
                        }
                        item{
                            Column(verticalArrangement = Arrangement.Center) {
                                val listFirstCategory = if (loadedCategories.isNotEmpty()) loadedCategories.first().category else null

                                if(listFirstCategory!=null) {
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
                                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 10.dp).fillMaxWidth(),
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
                                            onDismissRequest = { categoryListExpanded = false }
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
                                            year,
                                            pagerState.currentPage % 12
                                        )
                                    val ySteps = 25
                                    val xSteps = CalendarUtil.getDaysInMonth(
                                        pagerState.currentPage % 12,
                                        year
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
                                            if(index>24)
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
                                                "" }
                                        .build()


                                    val barChartData = BarChartData(
                                        backgroundColor = BackgroundSecondColor,
                                        chartData = barDataList,
                                        xAxisData = xAxisData,
                                        yAxisData = yAxisData,
                                        barStyle = BarStyle(20.dp, paddingBetweenBars = 3.dp),
                                    )
                                    Box(Modifier
                                        .height(300.dp)
                                        .background(BackgroundSecondColor)) {
                                        BarChart(
                                            modifier = Modifier
                                                .background(BackgroundSecondColor)
                                                .fillMaxWidth()
                                                .height(300.dp),
                                            barChartData = barChartData
                                        )
                                        Box(Modifier.fillMaxSize().background(brush = Brush.horizontalGradient(
                                                0f to Color.Transparent,
                                                0.9f to Color.Transparent,
                                                0.93f to  BackgroundSecondColor,
                                                1f to  BackgroundSecondColor
                                        )))
                                        Text(daysString, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp).align(Alignment.BottomStart))
                                    }
                                }
                            }
                        }
                        itemsIndexed(loadedCategories.filter { it.count > 0 }) { _, category ->
                            var expanded by remember {
                                mutableStateOf(false)
                            }
                            Card(
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                backgroundColor = CardCategoryColorSecond,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                CategoryCardContent(
                                    category,
                                    year,
                                    pagerState.currentPage % 12,
                                    activityViewModel,
                                    activityRecordViewModel,
                                    expanded
                                ) { expanded = !expanded }
                            }

                        }
                    }
                }
                Text(
                    text = stringResource(id = R.string.information),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .height(40.dp)
                        .background(brush = Brush.verticalGradient(
                            0f to BackgroundSecondColor,
                            0.7f to BackgroundSecondColor,
                            1f to Color.Transparent
                        ))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        })
    if(!isLoaded)
        Box(
            Modifier
                .fillMaxSize()
                .background(BackgroundColor)){
            CircularProgressIndicator(
                Modifier.align(Alignment.Center),
                color = MainColorSecondRed
            )
        }
}

@OptIn(ExperimentalMaterialApi::class)
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
                    .weight(1f)) {
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
                activityViewModel.getActivities(category.category.id).collectAsState(initial = emptyList())
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
                                val activityHourCount = activityRecordViewModel.getActivityRecordsCountByActivityInMonth(activity.id, year, month)
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
    val maxX = CalendarUtil.getDaysInMonth(month,year)
    list.add(BarData(Point(0f,0f)))
    if(category!=null)
    for(index in 1 .. maxX) {
        val count = activityRecordViewModel.getActivityRecordsCountByCategoryInDay(category.id,year, month, index)
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
    list.add(BarData(Point(maxX+1f,0f)))
    return list
}
suspend fun updateLoadedCategoriesCountList(
    loadedCategoriesCount: SnapshotStateMap<Int, List<RecordCountCategory>>,
    year: Int,
    month: Int,
    activityRecordViewModel: ActivityRecordViewModel,
    categoryViewModel: CategoryViewModel
) {
    loadedCategoriesCount[month] = categoryViewModel.getCategories().first().toList().map {category->
        val count = activityRecordViewModel.getActivityRecordsCountByCategoryInMonth(
            category.id,
            year,
            month,
        )
        RecordCountCategory(category, count)
    }.filter { it.count > 0 }
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
    ).first().map { record ->
        val day = CalendarUtil.getCurrentDay(record.timestamp)
        val hour = CalendarUtil.getCurrentHour(record.timestamp)
        val category = categoryViewModel.getCategoryById(
            activityViewModel.getActivityById(record.activityId)!!.categoryId
        )
        ActivityRecordView(day, hour, Color(category.color.toColorInt()))
    }
}




