package com.example.onehourapp.receivers

import android.R
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.onehourapp.MainActivity
import com.example.onehourapp.helpers.AddNotificationHelper
import com.example.onehourapp.helpers.AddNotificationHelper.notificationChannelId
import com.example.onehourapp.utils.CalendarUtil
import java.util.Calendar
import java.util.Locale.Category


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val hour = intent?.getIntExtra("hour", 0) ?: 8
        val notificationIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("day",  CalendarUtil.getCurrentDay())
            putExtra("hour", CalendarUtil.getCurrentHour())
        }
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            hour,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )
        Log.d("NotificationReceiver", "Current notified: $hour")
        val repeatedNotification: Notification = buildLocalNotification(context, pendingIntent)!!.build()

        AddNotificationHelper.getNotificationManager()?.notify(AddNotificationHelper.ALARM_TYPE_RTC, repeatedNotification)
    }
    fun buildLocalNotification(
        context: Context?,
        pendingIntent: PendingIntent?
    ): NotificationCompat.Builder? {
        val currentTime = Calendar.getInstance().timeInMillis
        val notification = NotificationCompat.Builder(context!!, notificationChannelId)
            .setCategory(Notification.CATEGORY_REMINDER)
            .setContentTitle("Как прошел ваш час?")
            .setContentText("Нажмите, чтобы добавить запись")
            .setSmallIcon(com.example.onehourapp.R.drawable.ic_logo_small)
            .setContentIntent(pendingIntent)
            .setShowWhen(true)
            .setWhen(currentTime)
            .setAutoCancel(true)
            .setGroup("MY_NOTIFICATION_GROUP")
        return notification
    }
}