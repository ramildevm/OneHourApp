package com.example.onehourapp.ui.screens.home.yearstat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowLeft
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.onehourapp.R
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.CircleColor
import com.example.onehourapp.utils.CalendarUtil
import kotlin.math.PI
import kotlin.math.abs
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
        var year by remember {
            mutableStateOf(CalendarUtil.getCurrentYear())
        }
        Row(modifier = Modifier
            .background(BackgroundColor)
            .fillMaxWidth()
            .height(40.dp)
            .zIndex(1f)){
            IconButton(onClick = {
                year--
            }) {
                Icon(imageVector = Icons.Rounded.ArrowLeft, null)
            }
            Text(year.toString(), modifier= Modifier
                .weight(1f)
                .fillMaxWidth(), textAlign = TextAlign.Center)
            IconButton(onClick = {
                year++
            }) {
                Icon(imageVector = Icons.Rounded.ArrowRight, null)
            }
        }

        Box(modifier = Modifier
            .weight(1f)
            .zIndex(0f)) {
            ZoomableCanvas(density = density, year = year)
        }
    }
}

@Composable
fun ZoomableCanvas(density: Float, year: Int) {
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
                    //val normalizedRotation = if (rotation >= 0) rotation else rotation + 360
                    offsetX = newOffsetX
                    offsetY = newOffsetY
                    canvasRotation += rotation
                    scale = newScale
                }
            }
            .wrapContentSize()
    ) {
        drawCanvasContent(this,density,year,months, scale, offsetX, offsetY, canvasRotation)
    }
}
private fun drawCanvasContent(
    drawScope: DrawScope,
    density: Float,
    year: Int,
    months: List<String>,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
    rotation: Float,
) {
    val canvasWidth = drawScope.size.width
    val canvasHeight = drawScope.size.height
    val dotRadius = 2f
    val dotSpacing = 2f
    val daysInMonths = arrayOf(31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365)
    val leapDay = if(year%4==0)1 else 0
    daysInMonths.forEachIndexed { index, element ->
        daysInMonths[index] = element + 1 + if(index!=0) leapDay else 0
    }
    val totalColumns = daysInMonths[11] + 2
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
        canvas.save()

        val angleY = (0 * (360f / totalColumns) * (PI / 180)).toFloat() - startRotation + canvasRotation
        val rowRadiusY = circleRadius + (27 * (dotRadius + dotSpacing)) * density
        val yOffset = abs(centerY + rowRadiusY * sin(angleY)) / Math.PI

        canvas.rotate(rotation)
        val paint = android.graphics.Paint().apply {
            textSize = 30f
            color = 0x88FFFFFF.toInt()
        }
        val path = android.graphics.Path().apply {
            addCircle(0f, 0f , circleRadius.toFloat(), android.graphics.Path.Direction.CW)
        }
        canvas.rotate(-90f)
        for (text_index in months.indices) {
            canvas.nativeCanvas.drawTextOnPath(months[text_index], path, 0f, -yOffset.toFloat(), paint)
            canvas.rotate(30f)
            when(text_index){
                0 -> canvas.rotate(2f)
                1 -> canvas.rotate(-3f + leapDay.toFloat())
                5 -> canvas.rotate(-leapDay.toFloat())
                9 -> canvas.rotate(-1f)
                11 -> canvas.rotate(-1f)
            }
        }
        canvas.restore()

        var currentMonth = 0
        var lastOffset:Offset? = null
        var firstOffset:Offset? = null
        var maxY = 0f
        var maxX = 0f
        for (i in 0 until totalColumns) {
            if(daysInMonths.contains(i) && currentMonth!=11)
                currentMonth++

            val angle = (i * (360f / totalColumns) * (PI / 180)).toFloat() - startRotation + canvasRotation

            for (row in 0 until 27) {
                val rowRadius = circleRadius + (row * (dotRadius + dotSpacing)) * density
                val x = centerX + rowRadius * cos(angle)
                val y = centerY + rowRadius * sin(angle)

                val linePaint = Paint().apply {
                    color = Color.White
                    alpha = 0.5f
                }
                if(i>totalColumns-3)
                    continue

                if(row==24){
                    if(daysInMonths.contains(i) || i==0 || i==totalColumns-3)
                        firstOffset = Offset(x, y)
                    continue
                }
                else if (row == 25){
                    if(daysInMonths.contains(i) || i==0 || i==totalColumns-3) {
                        canvas.drawLine(firstOffset!!, Offset(x, y), linePaint)
                        firstOffset = Offset(x, y)
                    }
                    continue
                }
                else if (row == 26){
                    canvas.drawLine(firstOffset!!, Offset(x, y), linePaint)
                    firstOffset = Offset(x,y)
                    if(daysInMonths.contains(i) || i==totalColumns-3)
                        canvas.drawLine(firstOffset!!, lastOffset!!, linePaint)
                    lastOffset = Offset(x,y)
                    continue
                }
                if(daysInMonths.contains(i))
                    continue

                val circlePaint = Paint().apply {
                    color = CircleColor

                }
                canvas.drawCircle(paint = circlePaint, center = Offset(x, y), radius = dotRadius)
            }
        }
        canvas.restore()
    }
}

