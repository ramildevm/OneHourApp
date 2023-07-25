package com.example.onehourapp.helpers

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.example.onehourapp.receivers.NotificationBootReceiver
import com.example.onehourapp.receivers.NotificationReceiver
import com.example.onehourapp.utils.SharedPreferencesUtil
import java.util.Calendar


object AddNotificationHelper {
    var ALARM_TYPE_RTC = 100
    const val notificationChannelId = "my_notification_channel"
    private var alarmManagerRTC: AlarmManager? = null
    private val alarmIntentRTCList: MutableList<PendingIntent> = mutableListOf()
    private var notificationManager: NotificationManager? = null
    fun scheduleRepeatingRTCNotification(context: Context, startHour:Int, stopHour: Int) {
        Log.d("AlarmNotificationHelper", "Schedule create: created")
        alarmManagerRTC = context.getSystemService(AlarmManager::class.java)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        val correctedStopHour = if (stopHour < startHour) stopHour + 24 else stopHour

        for (hour in startHour..correctedStopHour) {
            if(correctedStopHour>23)
            {
                calendar.set(Calendar.HOUR_OF_DAY, hour-24, 0)
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            else
                calendar.set(Calendar.HOUR_OF_DAY, hour, 0)

            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("hour", hour)
            }
            val alarmIntentRTC = PendingIntent.getBroadcast(
                context,
                hour,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmIntentRTCList.add(alarmIntentRTC)

            alarmManagerRTC = context.getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManagerRTC!!.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntentRTC
            )
            Log.d("AlarmNotificationHelper", "Schedule create: ${hour}: ${alarmIntentRTC.hashCode()}")
        }
        SharedPreferencesUtil.setSharedData(context, "isNotificationsRunning", true)
    }
    fun createNotificationChannel(context: Context) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("AlarmNotificationHelper", "Channel create: created")
            val channel = NotificationChannel(
                notificationChannelId,
                "Add Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }
    fun cancelAlarmRTC(context: Context) {
        alarmManagerRTC = context.getSystemService(ALARM_SERVICE) as AlarmManager
        if (alarmIntentRTCList.size != 0) {
            alarmIntentRTCList.forEach {
                alarmManagerRTC!!.cancel(it)
                Log.d("AlarmNotificationHelper", "Cancel: ${it.hashCode()}: ${it.javaClass}")
            }
        }
        else{
            val startHour = SharedPreferencesUtil.getSharedIntData(context!!,"start_hour",8)
            val stopHour = SharedPreferencesUtil.getSharedIntData(context!!,"end_hour",0)

            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()

            val correctedStopHour = if (stopHour < startHour) stopHour + 24 else stopHour

            for (hour in startHour..correctedStopHour) {
                if(correctedStopHour>23)
                {
                    calendar.set(Calendar.HOUR_OF_DAY, hour-24, 0)
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                else
                    calendar.set(Calendar.HOUR_OF_DAY, hour, 0)

                val intent = Intent(context, NotificationReceiver::class.java)
                val alarmIntentRTC = PendingIntent.getBroadcast(
                    context,
                    hour,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManagerRTC!!.cancel(alarmIntentRTC)
                Log.d("AlarmNotificationHelper", "Cancel: ${hour}: ${alarmIntentRTC.hashCode()}")
            }
        }
        SharedPreferencesUtil.setSharedData(context, "isNotificationsRunning", false)
    }
    fun getNotificationManager(): NotificationManager? {
        return notificationManager
    }
    fun enableBootReceiver(context: Context) {
        val receiver = ComponentName(context, NotificationBootReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        Log.d("AlarmNotificationHelper", "Boot receiver: enabled")
    }
    fun disableBootReceiver(context: Context) {
        val receiver = ComponentName(context, NotificationBootReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        Log.d("AlarmNotificationHelper", "Boot receiver: disabled")
    }
}