package com.example.onehourapp.helpers

import androidx.compose.ui.graphics.Color

object SortingHelper {
    private fun rgbToHsv(r: Float, g: Float, b: Float): FloatArray {
        val max = java.lang.Float.max(java.lang.Float.max(r, g), b)
        val min = java.lang.Float.min(java.lang.Float.min(r, g), b)
        val delta = max - min
        val hsv = FloatArray(3)
        hsv[0] = when {
            delta == 0f -> 0f
            max == r -> (60 * ((g - b) / delta + (if (g < b) 6 else 0))) % 360
            max == g -> (60 * ((b - r) / delta + 2)) % 360
            else -> (60 * ((r - g) / delta + 4)) % 360
        }
        hsv[1] = if (max == 0f) 0f else delta / max
        hsv[2] = max

        return hsv
    }

    private fun hexToColor(hex: String): Color {
        val hexCleaned = hex.trimStart('#')
        val colorInt = hexCleaned.toLong(16).toInt()
        return Color(colorInt)
    }


    fun compareColors(color1: String, color2: String) :Int {
        val rgb1 = hexToColor(color1)
        val rgb2 = hexToColor(color2)

        val hsv1 = rgbToHsv(rgb1.red, rgb1.green, rgb1.blue)
        val hsv2 = rgbToHsv(rgb2.red, rgb2.green, rgb2.blue)

        return hsv1[0].compareTo(hsv2[0])
    }
}