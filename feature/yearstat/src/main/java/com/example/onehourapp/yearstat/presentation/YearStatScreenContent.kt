package com.example.onehourapp.yearstat.presentation
import android.graphics.Typeface
import android.text.TextUtils
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material.icons.rounded.ArrowLeft
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.onehourapp.R
import com.example.onehourapp.data.models.dto.CategoryCount
import com.example.onehourapp.theme.ui.ActivityListItemFont2
import com.example.onehourapp.theme.ui.BackgroundColor
import com.example.onehourapp.theme.ui.BackgroundSecondColor
import com.example.onehourapp.theme.ui.CardActivityColor
import com.example.onehourapp.theme.ui.CardCategoryColorSecond
import com.example.onehourapp.theme.ui.CategoryListItemFont2
import com.example.onehourapp.theme.ui.CategoryListItemFont2Inner
import com.example.onehourapp.theme.ui.MainColorSecondRed
import com.example.onehourapp.theme.ui.PieChartLabelFont
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.example.onehourapp.common.utils.CalendarUtil
import hu.ma.charts.bars.HorizontalBarsChart
import hu.ma.charts.bars.data.HorizontalBarsData
import hu.ma.charts.bars.data.StackedBarData
import hu.ma.charts.bars.data.StackedBarEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun YearStatScreenContent(navController: NavHostController) {
    MainContent()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContent() {
    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )
    val scope = rememberCoroutineScope()
    val activityRecordViewModel: ActivityRecordViewModel = hiltViewModel()
    val activityViewModel: ActivityViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()

    val months = listOf(stringResource(R.string.january),
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

    var year by remember {
        mutableIntStateOf(1000)
    }
    ModalBottomSheetLayout(
        scrimColor = Color.Unspecified,
        sheetState = state,
        sheetShape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
        sheetBackgroundColor = com.example.onehourapp.theme.ui.BackgroundSecondColor,
        sheetContent = {
            Box(
                modifier = Modifier.background(com.example.onehourapp.theme.ui.BackgroundSecondColor))
            {
                val totalHours = (if (year % 4 == 0) 366 else 365) * 24f
                val categoriesCountInYearList =
                    activityRecordViewModel.getActivityRecordsCountListByCategoriesInYear(  // UseCase
                        year
                    ).collectAsState(
                        initial = emptyList()
                    )
                LazyColumn(
                    Modifier
                        .padding(top = 40.dp)
                        .padding(horizontal = 10.dp)) {
                    item {
                        val pieChartData = PieChartData(
                            slices = categoriesCountInYearList.value.map { value ->
                                PieChartData.Slice(
                                    label = value.name + String.format(
                                        " - %.1f",
                                        (value.count / totalHours * 100f)
                                    ) + "%",
                                    color = Color(value.color.toColorInt()),
                                    value = value.count.toFloat()
                                )
                            }.plus(
                                PieChartData.Slice(
                                    value = totalHours - categoriesCountInYearList.value.sumOf { it.count },
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
                                    backgroundColor = com.example.onehourapp.theme.ui.BackgroundSecondColor,
                                    sliceLabelEllipsizeAt = TextUtils.TruncateAt.MIDDLE
                                )
                            Column(
                                Modifier
                                    .height(450.dp)
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
                                        textStyle = com.example.onehourapp.theme.ui.PieChartLabelFont,
                                        colorBoxSize = 15.dp,
                                        textSize = 14.sp
                                    )
                                )
                                DonutPieChart(
                                    modifier = Modifier
                                        .background(com.example.onehourapp.theme.ui.BackgroundSecondColor)
                                        .fillMaxWidth()
                                        .weight(1f),
                                    pieChartData,
                                    pieChartConfig
                                )
                            }
                        }
                    }
                    item{
                        Text(
                            text = stringResource(id = R.string.by_months),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, start = 20.dp),
                            color = com.example.onehourapp.theme.ui.MainColorSecondRed
                        )
                    }
                    item {
                        val categoriesCountInMonthList = mutableListOf<List<CategoryCount>>()
                        val monthCount = if (year == com.example.onehourapp.common.utils.CalendarUtil.getCurrentYear()) com.example.onehourapp.common.utils.CalendarUtil.getCurrentMonth() else 11

                        for (month in 0..monthCount) {
                            categoriesCountInMonthList.add(
                                activityRecordViewModel.getActivityRecordsCountListByCategoriesInMonth( //UseCase
                                    year,
                                    month
                                ).collectAsState(
                                    initial = emptyList()
                                ).value
                            )
                        }
                        val data = HorizontalBarsData(
                            bars = categoriesCountInMonthList.mapIndexed { idx, values ->
                                val totalCount = 24f * com.example.onehourapp.common.utils.CalendarUtil.getDaysInMonth(idx, year)
                                val valuesCountSum = values.sumOf { it.count }.toFloat()
                                StackedBarData(
                                    title = AnnotatedString(months[idx]),
                                    entries = values.mapIndexed { _, value ->
                                        StackedBarEntry(
                                            text = AnnotatedString(value.name),
                                            value = value.count.toFloat(),
                                            color = Color(value.color.toColorInt())
                                        )
                                    }.plus(
                                        StackedBarEntry(
                                            text = AnnotatedString("N/A"),
                                            value = totalCount - valuesCountSum,
                                            color = Color.Gray
                                        )
                                    )
                                )
                            }.reversed()
                        )
                        HorizontalBarsChart(
                            modifier = Modifier.padding(
                                vertical = 4.dp,
                                horizontal = 4.dp
                            ),
                            data = data,
                            legendOffset = 20.dp,
                            legend = null,
                            divider = {
                                Divider(color = Color.LightGray)
                            },
                            textContent = {
                                Text(it, style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold))
                            },
                            valueContent = {
                                Text(it + AnnotatedString(" " + stringResource(id = R.string.hour_short)), style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Medium), color = Color.Red)
                            }
                        )

                    }
                    item {
                        Text(
                            text = stringResource(id = R.string.categories),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, bottom=10.dp, start = 20.dp),
                            color = com.example.onehourapp.theme.ui.MainColorSecondRed
                        )
                    }
                    itemsIndexed(categoriesCountInYearList.value){ _, category ->
                        var expanded by remember {
                            mutableStateOf(false)
                        }
                        Card(
                            modifier = Modifier.padding(
                                vertical = 4.dp,
                                horizontal = 4.dp
                            ),
                            backgroundColor = com.example.onehourapp.theme.ui.CardCategoryColorSecond,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            CategoryCardContent(
                                category,
                                year,
                                activityViewModel,
                                activityRecordViewModel,
                                expanded
                            ) { expanded = !expanded }
                        }
                    }
                }
                Text(
                    text = stringResource(id = R.string.information),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(vertical = 4.dp)
                        .align(Alignment.TopCenter)
                        .background(com.example.onehourapp.theme.ui.BackgroundSecondColor),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }){
        Column {
            Row(
                modifier = Modifier
                    .background(com.example.onehourapp.theme.ui.BackgroundColor)
                    .fillMaxWidth()
                    .height(40.dp)
                    .zIndex(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    year--
                }) {
                    Icon(imageVector = Icons.Rounded.ArrowLeft, null)
                }
                Text(
                    year.toString(), modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(), textAlign = TextAlign.Center
                )
                IconButton(onClick = {
                    year++
                }) {
                    Icon(imageVector = Icons.Rounded.ArrowRight, null)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .zIndex(0f)
            ) {
                LaunchedEffect(Unit) {
                    delay(200)
                    year = com.example.onehourapp.common.utils.CalendarUtil.getCurrentYear()
                }
                ZoomableCanvas(year, months, activityRecordViewModel, activityViewModel, categoryViewModel)
                FloatingActionButton(
                    modifier = Modifier
                        .padding(15.dp)
                        .align(Alignment.BottomStart),
                    contentColor = Color.White,
                    backgroundColor = com.example.onehourapp.theme.ui.MainColorSecondRed,
                    onClick = { scope.launch { state.show() } }
                ) {
                    Icon(imageVector = Icons.Outlined.PriorityHigh, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun CategoryCardContent(
    category: CategoryCount,
    year: Int,
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
                tint = Color(category.color.toColorInt())
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier
                        .weight(1f),
                    style = com.example.onehourapp.theme.ui.CategoryListItemFont2
                )
                val categoryHourCount = category.count
                Text(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterVertically),
                    text = categoryHourCount.toString() + " " + stringResource(id = R.string.hour_short),
                    style = com.example.onehourapp.theme.ui.CategoryListItemFont2Inner
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
                activityViewModel.getActivities(category.id) //UseCase
                    .collectAsState(initial = emptyList())
            Column(modifier = Modifier.background(com.example.onehourapp.theme.ui.CardCategoryColorSecond)) {
                activities.value.forEach { activity ->
                    key(activity.hashCode()) {
                        Card(
                            elevation = 4.dp,
                            modifier = Modifier
                                .background(com.example.onehourapp.theme.ui.CardCategoryColorSecond)
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(com.example.onehourapp.theme.ui.CardActivityColor)
                                    .padding(horizontal = 6.dp, 4.dp)
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .padding(vertical = 10.dp),
                                    imageVector = Icons.Rounded.Circle,
                                    contentDescription = "Icon",
                                    tint = Color(category.color.toColorInt())
                                )
                                Text(
                                    text = activity.name,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .weight(1f),
                                    style = com.example.onehourapp.theme.ui.ActivityListItemFont2
                                )
                                val activityHourCount =
                                    activityRecordViewModel.getActivityRecordsCountByActivityInYear( //UseCase
                                        activity.id,
                                        year
                                    )
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .align(Alignment.CenterVertically),
                                    text = activityHourCount.toString() + " " + stringResource(id = R.string.hour_short),
                                    style = com.example.onehourapp.theme.ui.CategoryListItemFont2Inner
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ZoomableCanvas(
    year: Int,
    months: List<String>,
    activityRecordViewModel: ActivityRecordViewModel,
    activityViewModel: ActivityViewModel,
    categoryViewModel: CategoryViewModel
) {
    val density = LocalDensity.current.density

    val records =
        Array(12) {
            Array(31) {
                Array(24) {
                    ""
                }
            }
    }
    activityRecordViewModel.getActivityRecordsByYear(year).collectAsState(initial = emptyList()).value.forEach{ record-> //UseCase
        val month = com.example.onehourapp.common.utils.CalendarUtil.getCurrentMonth(record.timestamp)
        val day = com.example.onehourapp.common.utils.CalendarUtil.getCurrentDay(record.timestamp)
        val hour = com.example.onehourapp.common.utils.CalendarUtil.getCurrentHour(record.timestamp)
        val category = categoryViewModel.getCategoryById(activityViewModel.getActivityById(record.activityId)!!.categoryId) //Common usecase
        records[month][day-1][hour] = category.color
    }

    val startScale = 0.47f
    var scale by remember { mutableFloatStateOf(startScale) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, newRotation ->
                    val newScale = (scale * zoom).coerceIn(startScale, 1.5f)
                    val newOffsetX = (offsetX + pan.x).coerceIn(
                        -(newScale - startScale) * size.width,
                        (newScale - startScale) * size.width
                    )
                    val newOffsetY = (offsetY + pan.y).coerceIn(
                        -(newScale - startScale) * size.height - size.height,
                        (newScale - startScale) *size.height + size.height
                    )
                    //val normalizedRotation = if (rotation >= 0) rotation else rotation + 360
                    offsetX = newOffsetX
                    offsetY = newOffsetY
                    rotation += newRotation
                    scale = newScale
                }
            }
            .wrapContentSize()
    ) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height
        val dotRadius = 2f
        val dotSpacing = 2f
        val daysInMonths = com.example.onehourapp.common.utils.CalendarUtil.getDaysInMonthArray(year)

        val leapDay = if (year % 4 == 0) 1 else 0
        daysInMonths.forEachIndexed { index, element ->
            daysInMonths[index] = element + if (index != 0) leapDay else 0
        }
        val totalColumns = daysInMonths[11] + 2
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        val circleRadius = 500
        val totalDegrees = (totalColumns * (360f / totalColumns) * (PI / 180)).toFloat()
        val startRotation = totalDegrees / 4
        val canvasRotation = totalDegrees / (360f / rotation)

        fun getYOffset(row:Int) : Double{
            val angleY = 1.4f // (0 * (360f / totalColumns) * (PI / 180)).toFloat() - startRotation + (totalDegrees / (360f / 1f))
            val rowRadiusY = 0 + (( row * (dotRadius + dotSpacing)) * density)
            return abs(centerY + rowRadiusY * sin(angleY)).toDouble()
        }

        this.drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(offsetX, offsetY)
            canvas.scale(scale, scale)
            canvas.save()

            canvas.rotate(rotation)

            val angleY = 1.5f// (0 * (360f / totalColumns) * (PI / 180)).toFloat() - startRotation + (totalDegrees / (360f / 1f))
            val rowRadiusY = circleRadius + (27 * (dotRadius + dotSpacing)) * density
            val yOffset = abs(centerY + rowRadiusY * sin(angleY)) /Math.PI

            val paint = android.graphics.Paint().apply {
                textSize = 30f
                color = 0x88FFFFFF.toInt()
            }
            val hourPaint = android.graphics.Paint().apply {
                textSize = 8f
                color = 0x88FFFFFF.toInt()
            }
            val path = android.graphics.Path().apply {
                addCircle(
                    0f,
                    0f,
                    circleRadius.toFloat(),
                    android.graphics.Path.Direction.CW
                )
            }
            canvas.rotate(-92.2f)
            for(hour in 23 downTo  0){
                canvas.nativeCanvas.drawTextOnPath(
                    String.format("-%02d-", hour),
                    path,
                    0f,
                    -getYOffset(23-hour).toFloat(),
                    hourPaint
                )
            }
            canvas.rotate(2f)
            for (text_index in months.indices) {
                canvas.nativeCanvas.drawTextOnPath(
                    months[text_index],
                    path,
                    0f,
                    -getYOffset(28).toFloat(),
                    paint
                )
                canvas.rotate(30f)
                when (text_index) {
                    0 -> canvas.rotate(0f)
                    1 -> canvas.rotate(-2f + leapDay.toFloat())
                    5 -> canvas.rotate(-leapDay.toFloat())
                }
            }
            canvas.restore()

            var currentMonth = 0
            var lastOffset: Offset? = null
            var firstOffset: Offset? = null
            for (i in 0 until totalColumns) {
                if (daysInMonths.contains(i)) {
                    currentMonth++
                }
                val angle = (i * (360f / totalColumns) * (PI / 180)).toFloat() - startRotation + canvasRotation

                for (row in 0 until 27) {
                    val rowRadius = circleRadius + (row * (dotRadius + dotSpacing)) * density
                    val x = centerX + rowRadius * cos(angle)
                    val y = centerY + rowRadius * sin(angle)

                    val linePaint = Paint().apply {
                        color = Color.White
                        alpha = 0.5f
                    }
                    if (i > totalColumns - 3)
                        continue

                    if (row == 24) {
                        if (daysInMonths.contains(i) || i == 0 || i == totalColumns - 3)
                            firstOffset = Offset(x, y)
                        continue
                    } else if (row == 25) {
                        if (daysInMonths.contains(i) || i == 0 || i == totalColumns - 3) {
                            canvas.drawLine(firstOffset!!, Offset(x, y), linePaint)
                            firstOffset = Offset(x, y)
                        }

                        continue
                    } else if (row == 26) {
                        canvas.drawLine(firstOffset!!, Offset(x, y), linePaint)
                        firstOffset = Offset(x, y)
                        if (daysInMonths.contains(i) || i == totalColumns - 3)
                            canvas.drawLine(firstOffset!!, lastOffset!!, linePaint)
                        lastOffset = Offset(x, y)
                        continue
                    }
                    if (daysInMonths.contains(i))
                        continue


                    val circlePaint = Paint().apply {
                        val day = i - if(currentMonth==0) 0 else ((daysInMonths[currentMonth-1] + 1))
                        val record = records[currentMonth][day][23-row]
                        color = if (record.isBlank()) Color.Gray  else Color(record.toColorInt())
                    }
                    canvas.drawCircle(
                        paint = circlePaint,
                        center = Offset(x, y),
                        radius = dotRadius
                    )
                }
            }
            canvas.restore()
        }

}
}

