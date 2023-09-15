package com.example.onehourapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalendarUtil {
    fun getDaysInMonth(month: Int, year: Int): Int {
        val calendar = Calendar.getInstance()

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
    fun getYearStartMillis(year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getMonthStartMillis(year: Int, month: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getDateTimestamp(year: Int, month: Int, dayOfMonth: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getDateAndTimeTimestamp(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

}