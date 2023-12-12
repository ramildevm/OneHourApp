package com.example.onehourapp.notification.receivers

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.onehourapp.NOTIFICATION_CURRENT_TIME
import com.example.onehourapp.R
import com.example.onehourapp.notification.NotificationsAlarmManager
import com.example.onehourapp.dialog.record.presentation.AddRecordDialogActivity
import com.example.onehourapp.domain.usecase.GetUserSettingsUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

const val NOTIFICATION_CHANNEL_ID = "channel1"
const val idExtra = "idExtra"

@AndroidEntryPoint
class NotificationReceiver: BroadcastReceiver()
{
    @Inject
    lateinit var getUserSettings: GetUserSettingsUseCase
    override fun onReceive(context: Context, intent: Intent)
    {
        val userSettings = getUserSettings()

        val currentHour = com.example.onehourapp.common.utils.CalendarUtil.getCurrentHour()
        val currentTimeValue = com.example.onehourapp.common.utils.CalendarUtil.getCurrentHourMillis()
        val notificationIntent = Intent(context, AddRecordDialogActivity::class.java).apply {
                putExtra(NOTIFICATION_CURRENT_TIME, currentTimeValue)
            }
        val pendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val actionIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra(NOTIFICATION_CURRENT_TIME, currentTimeValue)
            putExtra(idExtra, currentHour)
        }
        val actionPendingIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setCategory(Notification.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_logo)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_logo_image))
            .setContentTitle(context.getString(R.string.how_did_pass_hour))
            .setContentText(context.getString(R.string.click_to_add_record))
            .setAutoCancel(true)
            .apply { if(userSettings.lastAddedActivityId != 0) addAction(R.drawable.add_circle_icon, context.getString(
                            R.string.add_last_activity), actionPendingIntent) }
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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