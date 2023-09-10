package com.example.onehourapp.ui.helpers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue

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