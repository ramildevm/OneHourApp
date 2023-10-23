package com.example.onehourapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ClockProgressIndicator(isVisible:Boolean) {
    var rotationState by remember { mutableFloatStateOf(45f) }
    var rotationState2 by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Canvas(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f

            // Clear the canvas
            drawRect(Color.Transparent, size = size)

            drawCircle(
                color = Color.White,
                center = Offset(centerX, centerY),
                radius = (size.minDimension / 2) - 20.dp.toPx(),
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
            )
//            rotationState += 3f
//            rotationState2 += 6f // Adjust the rotation speed as needed

            // Calculate the position of the rectangle
            val rectWidth = 5.dp.toPx()
            val rectHeight = 10.dp.toPx()
            val rectLeft = centerX - 2.5.dp.toPx()
            val rectTop = centerY

            // Rotate and draw the white rectangle
            rotate(rotationState, pivot = Offset( centerX, centerY)) {
                drawRect(
                    color = Color.Red,
                    size = Size(rectWidth,rectHeight),
                    topLeft = Offset(rectLeft, rectTop)
                )
            }
            // Calculate the position of the rectangle
            val rectWidth2 = 5.dp.toPx()
            val rectHeight2 = 20.dp.toPx()
            val rectLeft2 = centerX - 2.5.dp.toPx()
            val rectTop2 = centerY

            // Rotate and draw the white rectangle
            rotate(rotationState2, pivot = Offset( centerX, centerY)) {
                drawRect(
                    color = Color.White,
                    size = Size(rectWidth2,rectHeight2),
                    topLeft = Offset(rectLeft2, rectTop2)
                )
            }
        }
        // Trigger the rotation animation
        LaunchedEffect(isVisible) {
            while (true) {
                coroutineScope.launch {
                    rotationState += 1f
                    rotationState2 += 2f
                }
                if(rotationState % 360f==45f){
                    delay(300)
                }
                else
                    delay(3)// Adjust the delay as needed for desired rotation speed
            }
        }
    }
}