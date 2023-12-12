package com.example.onehourapp.common.utils

import java.util.Calendar

object CalendarUtil {
    const val DAY_MILLIS = 24 * 60 * 60 * 1000L
    const val HOUR_MILLIS = 60 * 60 * 1000L
    fun getDaysInMonth(month: Int, year: Int = getCurrentYear()): Int {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    fun getDaysInMonthArray(year: Int): Array<Int> {
        var arrayList:Array<Int> = Array(12){0}
        val calendar = Calendar.getInstance()
        for(i in 0 until 12) {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, i)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            arrayList[i] = (if(i!=0) (arrayList[i-1] + 1) else 0) + calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        return arrayList
    }
    fun getCurrentYear(timestamp: Long = 0L): Int {
        val currentDate = Calendar.getInstance()
        if(timestamp != 0L)
            currentDate.timeInMillis = timestamp
        return currentDate.get(Calendar.YEAR)
    }
    fun getCurrentMonth(timestamp: Long = 0L): Int {
        val currentDate = Calendar.getInstance()
        if(timestamp != 0L)
            currentDate.timeInMillis = timestamp
        return currentDate.get(Calendar.MONTH)
    }
    fun getCurrentDay(timestamp: Long = 0L): Int {
        val currentDate = Calendar.getInstance()
        if(timestamp != 0L)
            currentDate.timeInMillis = timestamp
        return currentDate.get(Calendar.DAY_OF_MONTH)
    }

    fun getCurrentDayMillis(timestamp: Long = 0L): Long {
        val currentDate = Calendar.getInstance()
        if(timestamp!=0L)
            currentDate.timeInMillis = timestamp
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)
        return currentDate.timeInMillis
    }
    fun getHourCheckedCurrentDayMillis(): Long {
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)
        if(getCurrentHour() ==0)
            currentDate.timeInMillis -= DAY_MILLIS
        return currentDate.timeInMillis
    }
    fun getCurrentHour(timestamp: Long = 0L): Int {
        val currentDate = Calendar.getInstance()
        if(timestamp != 0L)
            currentDate.timeInMillis = timestamp
        return currentDate.get(Calendar.HOUR_OF_DAY)
    }
    fun getCurrentHourMillis(): Long {
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)
        return currentDate.timeInMillis
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
    fun getDayStartMillis(year: Int, month: Int, day:Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, 0, 0, 0)
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

    fun getFullDateTimeString(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return String.format(
            "%d-%d-%d %02d:00",
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.HOUR_OF_DAY)
        )
    }

}