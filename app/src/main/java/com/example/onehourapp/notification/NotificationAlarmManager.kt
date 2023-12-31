package com.example.onehourapp.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.onehourapp.common.utils.CalendarUtil
import com.example.onehourapp.notification.receivers.NotificationReceiver
import java.util.Calendar

class NotificationsAlarmManager(private val context: Context) {
    private val alarmMgr = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("ScheduleExactAlarm")
    fun startScheduleNotifications() {
        val currentHour = CalendarUtil.getCurrentHour()
        val intent = Intent(context, NotificationReceiver::class.java)
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = CalendarUtil.getCurrentHourMillis() + 3600000
        }
        alarmMgr.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            PendingIntent.getBroadcast(
                context,
                currentHour,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }
    fun cancelScheduleNotifications() {
        for (hour in 0..23)
            alarmMgr.cancel(
                PendingIntent.getBroadcast(
                    context,
                    hour,
                    Intent(context, NotificationReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                ),
            )
    }
}
