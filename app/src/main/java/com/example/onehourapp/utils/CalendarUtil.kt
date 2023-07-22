package com.example.onehourapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalendarUtil {
    fun getDaysInMonth(month: Int, year: Int): Int {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    fun getCurrentYear(): Int {
        val currentDate = Calendar.getInstance()
        return currentDate.get(Calendar.YEAR)
    }
    fun getCurrentMonth(): Int {
        val currentDate = Calendar.getInstance()
        return currentDate.get(Calendar.MONTH)
    }
    fun getCurrentDay(): Int {
        val currentDate = Calendar.getInstance()
        return currentDate.get(Calendar.DAY_OF_MONTH)
    }
    fun getCurrentHour(): Int {
        val currentDate = Calendar.getInstance()
        return currentDate.get(Calendar.HOUR_OF_DAY)
    }

}