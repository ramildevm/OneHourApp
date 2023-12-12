package com.example.onehourapp.notification

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.example.onehourapp.notification.receivers.NotificationBootReceiver


object NotificationBootReceiverHelper {
    fun enableBootReceiver(context: Context) {
        val receiver = ComponentName(context, NotificationBootReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }
    fun disableBootReceiver(context: Context) {
        val receiver = ComponentName(context, NotificationBootReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}