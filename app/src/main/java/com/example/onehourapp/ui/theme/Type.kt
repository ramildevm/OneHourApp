package com.example.onehourapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

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
val CalendarDayFont= TextStyle(
fontFamily = FontFamily.Monospace,
fontWeight = FontWeight.Normal,
    fontSize = 8.sp,
    color = Color.White,
    textAlign = TextAlign.Center
)
val CalendarCurrentDayFont= TextStyle(
fontFamily = FontFamily.Monospace,
fontWeight = FontWeight.Normal,
    fontSize = 8.sp,
    color = MainColorSecondRed,
    textAlign = TextAlign.Center
)