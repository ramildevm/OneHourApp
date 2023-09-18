package com.example.onehourapp.ui.screens.home.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first


@Composable
fun CalendarScreenContent(navController: NavHostController, isChanged: MutableState<Boolean>) {
    HorizontalPagerContent(navController, isChanged)
}

data class ActivityRecordView(
    val day: Int,
    val hour: Int,
    val color: Color
)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMotionApi::class, ExperimentalTextApi::class)
@Composable
fun HorizontalPagerContent(navController: NavHostController, isChanged: MutableState<Boolean>) {
    val context = LocalContext.current
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

    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.home_calendar_motion_scene)
            .readBytes()
            .decodeToString()
    }
    val year = remember {
        mutableStateOf(1000)
    }
    val motionState = rememberMotionLayoutState()
    MotionLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        motionScene = MotionScene(content = motionScene),
        motionLayoutState = motionState
    ) {
        LaunchedEffect(Unit) {
            delay(200)
            year.value = CalendarUtil.getCurrentYear()
        }
        Box(
            modifier = Modifier
                .background(BackgroundColor)
                .layoutId("calendar_panel")
        ) {
            val loadedData = remember {
                mutableStateMapOf<Int, List<ActivityRecordView>>()
            }
            val textMeasurer = rememberTextMeasurer()
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
                    .fillMaxSize()
                    .background(BackgroundColor),
                verticalAlignment = Alignment.CenterVertically,
                pageSpacing = 45.dp
            )
            { pageIndex ->
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
                        Text(
                            textAlign = TextAlign.Center,
                            color = Color.Red,
                            text = months[pageIndex % 12],
                            style = MainFont
                        )
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
        }
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(horizontal = 5.dp)
                .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .layoutId("content_panel")
        ) {
            val text = months[pagerState.currentPage % 12]
            Text(
                text = "$text \n" + generateLoremIpsumWords(30),
                modifier = Modifier
                    .background(BackgroundSecondColor)
                    .fillMaxHeight()
                    .padding(10.dp),
                style = MainFont,
                textAlign = TextAlign.Center
            )
        }
    }
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




