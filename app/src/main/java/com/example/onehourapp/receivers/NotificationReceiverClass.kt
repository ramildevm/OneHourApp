package com.example.onehourapp.receivers

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onehourapp.MainApp
import com.example.onehourapp.R
import com.example.onehourapp.data.database.dao.UserSettingsDao
import com.example.onehourapp.data.preferences.SharedPreferencesKeys
import com.example.onehourapp.data.repositories.UserSettingsRepository
import com.example.onehourapp.helpers.NotificationsAlarmManager
import com.example.onehourapp.ui.screens.AddRecordDialogActivity
import com.example.onehourapp.ui.viewmodels.UserSettingsViewModel
import com.example.onehourapp.utils.CalendarUtil
import com.example.onehourapp.utils.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.notify
import javax.inject.Inject
import javax.inject.Named

const val channelID = "channel1"
const val idExtra = "idExtra"
const val currentTime = "currentTimeExtra"

@AndroidEntryPoint
class NotificationReceiverClass : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        println("Alarm triggered: done")
        Log.e("NotReceiver", "received")
        val currentHour = CalendarUtil.getCurrentHour()
        Log.e("NotReceiver", "current hour: $currentHour")
        val notificationIntent = Intent(context, AddRecordDialogActivity::class.java).apply {
                putExtra(currentTime, CalendarUtil.getCurrentHourMillis())
            }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, channelID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_logo_small)
            .setContentTitle("Как прошел ваш час?")
            .setContentText("Нажмите, чтобы добавить запись $currentHour")
            .build()
        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val start = SharedPreferencesUtil.getSharedIntData(context.applicationContext, SharedPreferencesKeys.PREF_START_HOUR)
        val end = SharedPreferencesUtil.getSharedIntData(context.applicationContext, SharedPreferencesKeys.PREF_END_HOUR)
        Log.e("NotReceiver", "start end: $start $end")
        manager.notify(currentHour, notification)
//        if(start<end){
//            if(currentHour in start until end)
//                manager.notify(currentHour, notification)
//        }
//        else
//            if (currentHour !in end until start)

        val alarmManager = NotificationsAlarmManager(context)
        alarmManager.startScheduleNotifications()
    }
}