package com.example.onehourapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.onehourapp.data.preferences.SharedPreferencesKeys
import com.example.onehourapp.helpers.NotificationsAlarmManager
import com.example.onehourapp.utils.SharedPreferencesUtil

class NotificationBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val alarmManager = NotificationsAlarmManager(context)
            alarmManager.cancelScheduleNotifications()
            alarmManager.startScheduleNotifications()
        }
    }
}