package com.example.onehourapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import com.example.onehourapp.R
import com.example.onehourapp.notification.receivers.NOTIFICATION_CHANNEL_ID

object NotificationChannelBuilder {
    fun createNotificationChannel(applicationContext: Context) {
        val name = applicationContext.getString(R.string.notifier)
        val desc = applicationContext.getString(R.string.hour_notifiaction)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
        channel.description = desc
        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun deleteNotificationChannel(applicationContext: Context) {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
    }

    fun isNotificationChannelEnabled(context: Context): Boolean {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = manager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) ?: return false
        return channel.importance != NotificationManager.IMPORTANCE_NONE
    }
}
