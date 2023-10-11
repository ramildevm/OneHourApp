package com.example.onehourapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.onehourapp.data.models.Category

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
val MainFont = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    color = Color.White,
    textAlign = TextAlign.Center
)
val MainFontMedium = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    color = Color.White,
    textAlign = TextAlign.Center
)
val MainFontSmall = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    color = Color.White,
    textAlign = TextAlign.Center
)
val TextFieldStyle = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    color = Color.White
)
val BottomBarLabelFontRu = TextStyle(
fontFamily = FontFamily.Monospace,
fontWeight = FontWeight.Bold,
    fontSize = 7.9.sp,
    color = MainColorSecondRed,
    textAlign = TextAlign.Center
)
val BottomBarLabelFontEn = TextStyle(
fontFamily = FontFamily.Monospace,
fontWeight = FontWeight.Normal,
    fontSize = 9.sp,
    color = MainColorSecondRed,
    textAlign = TextAlign.Center
)
val CalendarDayFont = TextStyle(
fontFamily = FontFamily.Monospace,
fontWeight = FontWeight.Normal,
    fontSize = 8.sp,
    color = Color.White,
    textAlign = TextAlign.Center
)
val CalendarCurrentDayFont = TextStyle(
fontFamily = FontFamily.Monospace,
fontWeight = FontWeight.Normal,
    fontSize = 8.sp,
    color = MainColorSecondRed,
    textAlign = TextAlign.Center
)
val CategoryListItemFont = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp
)
val CategoryListItemFont2 = TextStyle(
    color = Color.White,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    letterSpacing = 0.15.sp,
)
val CategoryListItemFont2Inner = TextStyle(
    color = Color.White,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 0.15.sp,
)
val ActivityListItemFont = TextStyle(
    color = Color.White,
    fontWeight = FontWeight.Medium,
    fontSize = 17.sp,
    letterSpacing = 0.15.sp,
)
val ActivityListItemFont2 = TextStyle(
    color = Color.White,
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
    letterSpacing = 0.15.sp,
)

val PieChartLabelFont = TextStyle(
    color = Color.White,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    letterSpacing = 0.15.sp,
)