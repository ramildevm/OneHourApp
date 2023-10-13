package com.example.onehourapp.receivers

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.onehourapp.R
import com.example.onehourapp.data.database.dao.UserSettingsDao
import com.example.onehourapp.helpers.NotificationsAlarmManager
import com.example.onehourapp.ui.screens.AddRecordDialogActivity
import com.example.onehourapp.utils.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

const val channelID = "channel1"
const val idExtra = "idExtra"
const val currentTime = "currentTimeExtra"

@AndroidEntryPoint
class NotificationReceiverClass : BroadcastReceiver()
{
    @Inject
    @Named("userSettings")
    lateinit var userSettingsDao:UserSettingsDao
    override fun onReceive(context: Context, intent: Intent)
    {
        val currentHour = CalendarUtil.getCurrentHour()
        val notificationIntent = Intent(context, AddRecordDialogActivity::class.java).apply {
                putExtra(currentTime, CalendarUtil.getCurrentHourMillis())
            }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, channelID)
            .setCategory(Notification.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_logo_notification)
            .setContentTitle(context.getString(R.string.how_did_pass_hour))
            .setContentText(context.getString(R.string.click_to_add_record))
            .setAutoCancel(true)
            .build()
        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val userSettings = userSettingsDao.getUserSettings()
        val start = userSettings.notificationStartHour
        val end = userSettings.notificationEndHour
        if(start<end){
            if(currentHour in start until  end)
                manager.notify(currentHour, notification)
        }
        else
            if (currentHour !in end until start)
                manager.notify(currentHour, notification)

        val alarmManager = NotificationsAlarmManager(context)
        alarmManager.startScheduleNotifications()
    }
}