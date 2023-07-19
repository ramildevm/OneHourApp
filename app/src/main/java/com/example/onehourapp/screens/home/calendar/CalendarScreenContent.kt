package com.example.onehourapp.screens.home.calendar

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.rememberMotionLayoutState
import androidx.navigation.NavHostController
import com.example.onehourapp.R
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.BackgroundSecondColor
import com.example.onehourapp.ui.theme.CalendarCurrentDayFont
import com.example.onehourapp.ui.theme.CalendarDayFont
import com.example.onehourapp.ui.theme.MainFont
import com.example.onehourapp.utils.CalendarUtil.getCurrentDay
import com.example.onehourapp.utils.CalendarUtil.getCurrentMonth
import com.example.onehourapp.utils.CalendarUtil.getCurrentYear
import com.example.onehourapp.utils.CalendarUtil.getDaysInMonth
import kotlin.math.absoluteValue


@Composable
fun CalendarScreenContent(navController: NavHostController) {
    HorizontalPagerContent(navController)
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMotionApi::class, ExperimentalTextApi::class)
@Composable
fun HorizontalPagerContent(navController: NavHostController) {
    val context = LocalContext.current
    val months = listOf("January", "February", "March", "April", "May", "June","July","August","September","October","November","December")
    val color = listOf(Color.Red,
        Color(0xFFFF8800),
        Color.Yellow,
        Color.Green,
        Color(0xFF0088FF),
        Color.Blue,
        Color(0xFF8000FF),
        Color.Black,
        Color.DarkGray,
        Color.Gray,
        Color.LightGray,
        Color(0xFFFF8D8D)
    )
    val startIndex = Int.MAX_VALUE / 2 - 3 + getCurrentMonth()

    val pagerState = rememberPagerState(initialPage = startIndex)
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.home_calendar_motion_scene)
            .readBytes()
            .decodeToString()
    }
    val motionState = rememberMotionLayoutState()
    MotionLayout(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundColor),
        motionScene = MotionScene(content = motionScene),
        motionLayoutState = motionState
    ) {
        val painter: Painter = painterResource(R.drawable.round_scale_out)

        Box(modifier = Modifier
            .background(BackgroundColor)
            .layoutId("calendar_panel")
        ){
            val textMeasurer = rememberTextMeasurer()
            HorizontalPager(
                pageCount = Int.MAX_VALUE,
                state = pagerState,
                beyondBoundsPageCount = 2,
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor),
                verticalAlignment = Alignment.CenterVertically,
                pageSpacing = 45.dp
            )
            { pageIndex ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .pagerFanTransition(pageIndex, pagerState),
                ) {
                    val columns = getDaysInMonth(pageIndex%12, getCurrentYear())
                    val rows = 25
                    val spacing = 5.dp
                    val isCurrentMonth = pageIndex%12 == getCurrentMonth()
                    Text(
                        textAlign = TextAlign.Center,
                        color = Color.Red,
                        text = months[pageIndex%12],
                        style = MainFont
                    )
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        drawRect(
                            color= BackgroundColor,
                            size=Size(size.width,size.height)
                        )
                        val cellSize = (size.width - 5 * (columns + 1)) / columns
                        for (row in 0 until rows) {
                            for (column in 0 until columns) {
                                val day = column + 1
                                val isCurrentDay = (day == getCurrentDay() && isCurrentMonth)
                                var x = column * (cellSize + 5)
                                var y = row * (cellSize + 5)
                                val center = (columns-1) / 2
                                Log.e("Tag", "$center")
                                var offset = 0
                                when(column){
                                    in 0..center-2 -> offset = column
                                    in center-2..center+2 -> offset = center -2
                                    in center+2..columns -> offset = columns - column
                                }
                                Log.e("Tag", "$offset")
                                if(row!=0) {
                                    y += 5
                                    drawRoundRect(
                                        color = Color.LightGray,
                                        topLeft = Offset(x + spacing.toPx(), y - offset + spacing.toPx() ),
                                        size = Size(cellSize, cellSize),
                                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                                    )
                                }
                                else {
                                    var textOffset = 0
                                    if((column+1)%10==0)
                                        textOffset = 2
                                    if(column+1==columns)
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
        IconButton(
            onClick = {

            },
            modifier = Modifier.size(50.dp)
                .layoutId("scale_out_btn")
                .clip(CircleShape)
                .alpha(0.5f)
                .background(Color.DarkGray)
                .border(2.dp, Color.White, CircleShape),
            content = {
                Image(
                    painter = painter,
                    contentDescription = null,
                )
            }
        )
        Box(modifier = Modifier
            .background(Color.Transparent)
            .padding(horizontal = 5.dp)
            .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            .layoutId("content_panel")){
            val text = months[pagerState.currentPage % 12]
            Text(
                text ="$text \n" + generateLoremIpsumWords(30),
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
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerFanTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    cameraDistance = 200f
    val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
    translationY = pageOffset.absoluteValue * size.height / 8

    if (pageOffset < -1f) {
        alpha = 0f
    } else if (pageOffset < 0) {
        alpha = 1f
        rotationZ = 20f * pageOffset.absoluteValue
    } else if (pageOffset <= 1 && pageOffset>0) {
        alpha = 1f
        rotationZ = -20f * pageOffset.absoluteValue
    } else {
        alpha = 1f
    }
}
fun generateLoremIpsumWords(count: Int): String {
    val words = listOf(
        "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
        "adipiscing", "elit", "sed", "do", "eiusmod", "tempor",
        "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua"
    )

    return List(count) { words.random() }.joinToString(" ")
}
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}
