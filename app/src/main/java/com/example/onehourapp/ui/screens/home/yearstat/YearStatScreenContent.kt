package com.example.onehourapp.ui.screens.home.yearstat
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.onehourapp.R
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.example.onehourapp.utils.CalendarUtil
import kotlinx.coroutines.delay
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
            mutableIntStateOf(1000)
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
            LaunchedEffect(Unit){
                delay(200)
                year = CalendarUtil.getCurrentYear()
            }
            ZoomableCanvas(density = density, year = year)
        }
    }
}

@Composable
fun ZoomableCanvas(density: Float, year: Int) {
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
    val records =
        Array(12) {
            Array(31) {
                Array(24) {
                    ""
                }
            }
    }
    activityRecordViewModel.getActivityRecordsByYear(year).collectAsState(initial = emptyList()).value.forEach{ record->
        val month = CalendarUtil.getCurrentMonth(record.timestamp)
        val day = CalendarUtil.getCurrentDay(record.timestamp)
        val hour = CalendarUtil.getCurrentHour(record.timestamp)
        val category = categoryViewModel.getCategoryById(activityViewModel.getActivityById(record.activityId).categoryId)
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
                detectTransformGestures { _, pan, zoom, newRotation->
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
        val daysInMonths = CalendarUtil.getDaysInMonthArray(year)

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
            for(hour in 0..23){
                canvas.nativeCanvas.drawTextOnPath(
                    String.format("-%02d-", hour),
                    path,
                    0f,
                    -getYOffset(hour).toFloat(),
                    hourPaint
                )
            }
            canvas.rotate(2f)
            for (text_index in months.indices) {
                canvas.nativeCanvas.drawTextOnPath(
                    months[text_index],
                    path,
                    0f,
                    -yOffset.toFloat(),
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

