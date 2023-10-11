package com.example.onehourapp.ui.components

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private fun <T> getItemIndexForOffset(
    range: List<T>,
    value: T,
    offset: Float,
    halfNumbersColumnWidthPx: Float
): Int {
    val indexOf = range.indexOf(value) - (offset / halfNumbersColumnWidthPx).toInt()
    return maxOf(0, minOf(indexOf, range.count() - 1))
}

@Composable
fun <T> HorizontalListItemPicker(
    modifier: Modifier = Modifier,
    label: (T) -> String = { "0"+ it.toString() },
    value: T,
    onValueChange: (T) -> Unit,
    dividersColor: Color = MaterialTheme.colors.primary,
    list: List<T>,
    textStyle: TextStyle = LocalTextStyle.current,
    enabled:Boolean
) {
    val minimumAlpha = 0.3f
    val horizontalMargin = 8.dp
    val numbersColumnWidth = 80.dp
    val halfNumbersColumnWidth = numbersColumnWidth / 2
    val halfNumbersColumnWidthPx = with(LocalDensity.current) { halfNumbersColumnWidth.toPx() }

    val coroutineScope = rememberCoroutineScope()

    val animatedOffset = remember { Animatable(0f) }
        .apply {
            val index = list.indexOf(value)
            val offsetRange = remember(value, list) {
                -((list.count()-1) - index) * halfNumbersColumnWidthPx to
                        index * halfNumbersColumnWidthPx
            }
            updateBounds(offsetRange.first, offsetRange.second)
        }

    val coercedAnimatedOffset = animatedOffset.value % halfNumbersColumnWidthPx

    val indexOfElement = getItemIndexForOffset(list, value, animatedOffset.value, halfNumbersColumnWidthPx)

    var dividersWidth by remember { mutableStateOf(0.dp) }

    Layout(
        modifier = modifier
            .draggable(
                enabled = enabled,
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { deltaX ->
                    coroutineScope.launch {
                        animatedOffset.snapTo(animatedOffset.value + deltaX)
                    }
                },
                onDragStopped = { velocity ->
                    coroutineScope.launch {
                        val endValue = animatedOffset.fling(
                            initialVelocity = velocity,
                            animationSpec = exponentialDecay(frictionMultiplier = 20f),
                            adjustTarget = { target ->
                                val coercedTarget = target % halfNumbersColumnWidthPx
                                val coercedAnchors =
                                    listOf(-halfNumbersColumnWidthPx, 0f, halfNumbersColumnWidthPx)
                                val coercedPoint = coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
                                val base = halfNumbersColumnWidthPx * (target / halfNumbersColumnWidthPx).toInt()
                                coercedPoint + base
                            }
                        ).endState.value

                        val result = list.elementAt(
                            getItemIndexForOffset(list, value, endValue, halfNumbersColumnWidthPx)
                        )
                        onValueChange(result)
                        animatedOffset.snapTo(0f)
                    }
                }
            )
            .padding(horizontal = numbersColumnWidth / 3 + horizontalMargin * 2),
        content = {
            Box(
                modifier
                    .width(2.dp)
                    .height(dividersWidth)
                    .background(color = dividersColor)
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = horizontalMargin, vertical = 10.dp)
                    .offset { IntOffset(y = 0, x = coercedAnimatedOffset.roundToInt()) }
            ) {
                val baseLabelModifier = Modifier.align(Alignment.Center)
                ProvideTextStyle(textStyle) {
                    if (indexOfElement > 0)
                        if(coercedAnimatedOffset.toInt()>-30)
                        Label(
                            text = label(list.elementAt(indexOfElement - 1)),
                            modifier = baseLabelModifier
                                .offset(x = -halfNumbersColumnWidth)
                                .alpha(maxOf(minimumAlpha, coercedAnimatedOffset / halfNumbersColumnWidthPx))
                        )
                    Label(
                        text = label(list.elementAt(indexOfElement)),
                        modifier = baseLabelModifier
                            .alpha(
                                (maxOf(
                                    minimumAlpha,
                                    1 - abs(coercedAnimatedOffset) / halfNumbersColumnWidthPx
                                ))
                            )
                    )
                    if (indexOfElement < list.count() - 1)
                        if(coercedAnimatedOffset.toInt()<30)
                        Label(
                            text = label(list.elementAt(indexOfElement + 1)),
                            modifier = baseLabelModifier
                                .offset(x = halfNumbersColumnWidth)
                                .alpha(maxOf(minimumAlpha, -coercedAnimatedOffset / halfNumbersColumnWidthPx))
                        )
                }
            }
            Box(
                modifier
                    .width(2.dp)
                    .height(dividersWidth)
                    .background(color = dividersColor)
            )
        }
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        dividersWidth = placeables
            .drop(1)
            .first()
            .height
            .toDp()

        // Set the size of the layout as big as it can
        layout(dividersWidth.toPx().toInt(), placeables
            .sumOf {
                it.width
            }
        ) {
            // Track the y co-ord we have placed children up to
            var xPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->

                // Position item on the screen
                placeable.placeRelative(y = 0, x = xPosition)

                // Record the y co-ord placed up to
                xPosition += placeable.width
            }
        }
    }
}

@Composable
private fun Label(text: String, modifier: Modifier) {
    Text(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                // FIXME: Empty to disable text selection
            })
        },
        text = text,
        textAlign = TextAlign.Center,
    )
}

private suspend fun Animatable<Float, AnimationVector1D>.fling(
    initialVelocity: Float,
    animationSpec: DecayAnimationSpec<Float>,
    adjustTarget: ((Float) -> Float)?,
    block: (Animatable<Float, AnimationVector1D>.() -> Unit)? = null,
): AnimationResult<Float, AnimationVector1D> {
    val targetValue = animationSpec.calculateTargetValue(value, initialVelocity)
    val adjustedTarget = adjustTarget?.invoke(targetValue)
    return if (adjustedTarget != null) {
        animateTo(
            targetValue = adjustedTarget,
            initialVelocity = initialVelocity,
            block = block
        )
    } else {
        animateDecay(
            initialVelocity = initialVelocity,
            animationSpec = animationSpec,
            block = block,
        )
    }
}
