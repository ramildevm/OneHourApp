package com.example.onehourapp.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import com.example.onehourapp.receivers.channelID

object NotificationChannelBuilder {
    fun createNotificationChannel(applicationContext: Context, ) {
        val name = "Напоминалка"
        val desc = "Часовое напоминание"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun deleteNotificationChannel(applicationContext: Context) {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(channelID)
    }

    fun isNotificationChannelEnabled(context: Context): Boolean {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = manager.getNotificationChannel(channelID) ?: return false
        return channel.importance != NotificationManager.IMPORTANCE_NONE
    }
}
