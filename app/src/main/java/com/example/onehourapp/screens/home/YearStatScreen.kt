package com.example.onehourapp.screens.home
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.onehourapp.screens.home.activity.RoundedBoxesList
import com.example.onehourapp.ui.theme.BackgroundColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun YearStatScreenContent(navController: NavHostController) {
    MainContent()
}

@Composable
fun MainContent() {
    Column {
        val density = LocalDensity.current.density
        Box(modifier = Modifier.background(Color.Gray).fillMaxWidth().height(30.dp).zIndex(1f)){
            Text("DEDEDEDDDDDDDDE")
        }
        Box(modifier = Modifier.weight(1f).zIndex(0f)) {
            ZoomableCanvas(density = density)
        }
    }
}

@Composable
fun ZoomableCanvas(density:Float) {
    val startScale = 0.47f
    var scale by remember { mutableStateOf(startScale) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var canvasRotation by remember { mutableStateOf(0f) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotation ->
                    val newScale = (scale * zoom).coerceIn(startScale, 1.5f)
                    val newOffsetX = (offsetX + pan.x).coerceIn(
                        -(newScale - startScale) * size.width,
                        (newScale - startScale) * size.width
                    )
                    val newOffsetY = (offsetY + pan.y).coerceIn(
                        -(newScale - startScale) * size.height,
                        (newScale - startScale) * size.height
                    )
                    val normalizedRotation = if (rotation >= 0) rotation else rotation + 360
                    offsetX = newOffsetX
                    offsetY = newOffsetY
                    canvasRotation += normalizedRotation
                    scale = newScale
                }
            }
            .wrapContentSize()
    ) {
        drawCanvasContent(this,density, scale, offsetX, offsetY, canvasRotation)
    }
}
private fun drawCanvasContent(drawScope: DrawScope, density:Float, scale: Float, offsetX: Float, offsetY: Float, rotation: Float) {
    val canvasWidth = drawScope.size.width
    val canvasHeight = drawScope.size.height
    val dotRadius = 2f
    val dotSpacing = 2f
    val totalColumns = 372
    val centerX = canvasWidth / 2
    val centerY = canvasHeight / 2
    val circleRadius = 500
    val totalDegrees = (totalColumns * (360f / totalColumns) * (PI / 180)).toFloat()
    val startRotation = totalDegrees / 4
    val canvasRotation = totalDegrees / (360f/rotation)

    drawScope.drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX, offsetY)
        canvas.scale(scale, scale)
        for (row in 0 until 24) {
            var index = 0
            val rowRadius = circleRadius + (row * (dotRadius + dotSpacing)) * density
            for (i in 0 until totalColumns) {
                val angle = (i * (360f / totalColumns) * (PI / 180)).toFloat() - startRotation +canvasRotation

                val x = centerX + rowRadius * cos(angle)
                val y = centerY + rowRadius * sin(angle)
                var paintColor = Color.Red
                if(i==0)
                    paintColor = Color.Yellow
                else if(index%31==0){
                    index++
                    continue
                }
                val circlePaint = Paint().apply {
                    color = paintColor
                }
                canvas.drawCircle(paint = circlePaint, center = Offset(x, y), radius = dotRadius)
                index++
            }
        }
        canvas.restore()
    }
}