package com.example.onehourapp.helpers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.NotificationUtil.Importance
import com.example.onehourapp.data.preferences.SharedPreferencesKeys
import com.example.onehourapp.receivers.NotificationReceiverClass
import com.example.onehourapp.receivers.channelID
import com.example.onehourapp.receivers.idExtra
import com.example.onehourapp.utils.CalendarUtil
import com.example.onehourapp.utils.SharedPreferencesUtil
import kotlinx.coroutines.coroutineScope
import java.util.Calendar

class NotificationsAlarmManager(private val  context: Context){
    private val alarmMgr = context.getSystemService(AlarmManager::class.java)
    fun startScheduleNotifications(){
        Log.d("NotBuilder", "Schedule create: creating")

        val currentHour = CalendarUtil.getCurrentHour()
        val nextHour = CalendarUtil.getCurrentHour(CalendarUtil.getCurrentHourMillis() + 3600000)
        val start = SharedPreferencesUtil.getSharedIntData(context.applicationContext, SharedPreferencesKeys.PREF_START_HOUR)
        val end = SharedPreferencesUtil.getSharedIntData(context.applicationContext, SharedPreferencesKeys.PREF_END_HOUR)

        val intent = Intent(context, NotificationReceiverClass::class.java)
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = if(start < end) {
                if (nextHour > end){
                    CalendarUtil.getCurrentDayMillis() + 3600000 * (start + 24)
                } else if(nextHour < start)
                    CalendarUtil.getCurrentDayMillis() + 3600000 * start
                else
                    CalendarUtil.getCurrentHourMillis() + 3600000
            } else{
                if(nextHour < end || nextHour >= start)
                    CalendarUtil.getCurrentHourMillis() + 3600000
                else
                    CalendarUtil.getCurrentDayMillis() + 3600000 * start
            }
        }
        Log.d("NotBuilder", "Schedule create: time millis - ${calendar.get(Calendar.DAY_OF_MONTH)} ${calendar.get(Calendar.HOUR_OF_DAY)}")
        alarmMgr.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            PendingIntent.getBroadcast(
                context,
                currentHour,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.d("NotBuilder", "Schedule: created")
    }
    fun cancelScheduleNotifications(){
        for(hour in 0..23)
            alarmMgr.cancel(
                PendingIntent.getBroadcast(
                    context,
                    hour,
                    Intent(context, NotificationReceiverClass::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        Log.d("NotBuilder", "Schedule: canceled")
    }
}

fun scheduleNotification(applicationContext:Context)
{
    Log.d("NotBuilder", "Schedule create: creating")
    val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val startHour = CalendarUtil.getCurrentHourMillis() + 3600000
    for(hour in 0..23) {
        val intent = Intent(applicationContext, NotificationReceiverClass::class.java)
        intent.putExtra(idExtra, hour)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            hour,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startHour + 3600000 * hour,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.d("NotBuilder", "Schedule hour $hour: created")
    }
    Log.d("NotBuilder", "Schedule create: created")
}

fun createNotificationChannel(applicationContext: Context,)
{
    Log.d("NotBuilder", "Chanel create: creating")
    val name = "Напоминалка"
    val desc = "Часовое напоминание"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelID, name, importance)
    channel.description = desc
    val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
    Log.d("NotBuilder", "Chanel create: created")
}
fun deleteNotificationChannel(applicationContext: Context)
{
    val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.deleteNotificationChannel(channelID)
    Log.d("NotBuilder", "Chanel create: deleted")
}
fun isNotificationChannelEnabled(context: Context): Boolean {
    val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val channel = manager.getNotificationChannel(channelID) ?: return false
    return channel.importance != NotificationManager.IMPORTANCE_NONE

}
