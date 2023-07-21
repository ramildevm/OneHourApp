package com.example.onehourapp.screens.home
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.navigation.NavHostController
import com.example.onehourapp.screens.home.activity.RoundedBoxesList
import com.example.onehourapp.ui.theme.BackgroundColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun YearStatScreenContent(navController: NavHostController) {
    val density = LocalDensity.current.density
    ZoomableCanvas(density = density)
}
@Composable
fun ZoomableCanvas(density:Float) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
            .wrapContentSize()
    ) {
        drawCanvasContent(this,density, scale, offsetX, offsetY)
    }
}
private fun drawCanvasContent(drawScope: DrawScope, density:Float, scale: Float, offsetX: Float, offsetY: Float) {
    val canvasWidth = drawScope.size.width
    val canvasHeight = drawScope.size.height
    val dotRadius = 2f
    val dotSpacing = 2f
    val totalColumns = 372
    val centerX = canvasWidth / 2
    val centerY = canvasHeight / 2
    val circleRadius = 500

    drawScope.drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX, offsetY)
        canvas.scale(scale, scale)
        val paint = Paint().apply {
            color = Color.Green
        }
        canvas.drawRect(Rect(Offset(0f,0f),size = Size(canvasWidth,canvasHeight)), paint)
        for (row in 0 until 24) {
            var index = 0
            val rowRadius = circleRadius + (row * (dotRadius + dotSpacing)) * density
            for (i in 0 until totalColumns) {
                val angle = (i * (360f / totalColumns) * (PI / 180)).toFloat()
                val x = centerX + rowRadius * cos(angle)
                val y = centerY + rowRadius * sin(angle)
                var colore = Color.Red
                if(index%31==0)
                    colore = BackgroundColor
                val circlePaint = Paint().apply {
                    color = colore
                }
                canvas.drawCircle(paint = circlePaint, center = Offset(x, y), radius = dotRadius)
                index++
            }
        }
        canvas.restore()
    }
}