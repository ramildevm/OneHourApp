package com.example.onehourapp.notification.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.onehourapp.notification.NotificationsAlarmManager

class NotificationBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val alarmManager =
                NotificationsAlarmManager(context)
            alarmManager.cancelScheduleNotifications()
            alarmManager.startScheduleNotifications()
        }
    }
}