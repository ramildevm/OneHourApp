package com.example.onehourapp.screens.home.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.onehourapp.graphs.DetailsScreen
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.BackgroundSecondColor
import com.example.onehourapp.ui.theme.MainFont
import kotlin.math.absoluteValue


@Composable
fun CalendarScreenContent(navController: NavHostController) {
    HorizontalPagerContent(navController)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerContent(navController: NavHostController) {
    val items = listOf("J", "F", "M", "A", "M", "J","J","A","S","O","N","D")
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
    val startIndex = Int.MAX_VALUE / 2 - 3
    val pagerState = rememberPagerState(initialPage = startIndex)
    Box(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundColor)
    ) {
        HorizontalPager(
            pageCount = Int.MAX_VALUE,
            state = pagerState,
            beyondBoundsPageCount = 2,
            modifier = Modifier
                .background(BackgroundColor)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            pageSpacing = 20.dp
        )
        { pageIndex ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(20.dp)
                .pagerFanTransition(pageIndex, pagerState),
                shape = RoundedCornerShape(20.dp)
            ){
                Box(modifier = Modifier
                    .background(color[pageIndex % 12])
                    .fillMaxSize()){
                    Text(modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.White)
                        .padding(10.dp),
                        text = items[pageIndex % 12],
                        style = MainFont,
                        color = Color.Black
                    )
                }
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(0.dp, 290.dp)
                .align(Alignment.BottomCenter)
                .background(Color.Transparent)
                .padding( horizontal = 5.dp)
                .clip(RoundedCornerShape(15.dp,15.dp,0.dp,0.dp)),
        ) {
            val text = items[pagerState.currentPage % 12]
            Text(
                text ="$text \n" + generateLoremIpsumWords(30),
                modifier = Modifier
                    .background(BackgroundSecondColor)
                    .padding(10.dp)
                    .clickable {
                        navController.navigate(DetailsScreen.Information.route)
                    }
            )
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerFanTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    cameraDistance = 200f
    val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
    translationY = pageOffset.absoluteValue * size.height / 4

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
