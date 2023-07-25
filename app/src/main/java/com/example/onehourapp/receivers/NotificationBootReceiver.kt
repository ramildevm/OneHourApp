package com.example.onehourapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.onehourapp.helpers.AddNotificationHelper
import com.example.onehourapp.utils.SharedPreferencesUtil

class NotificationBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val start = SharedPreferencesUtil.getSharedIntData(context!!,"start_hour",8)
            val end = SharedPreferencesUtil.getSharedIntData(context!!,"end_hour",0)

            AddNotificationHelper.cancelAlarmRTC(context!!)
            AddNotificationHelper.scheduleRepeatingRTCNotification(context!!, start, end);
        }
    }
}