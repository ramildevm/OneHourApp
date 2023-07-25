package com.example.onehourapp.services

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.onehourapp.R
import com.example.onehourapp.receivers.NotificationReceiver
import com.example.onehourapp.screens.AddRecordDialogActivity
import com.example.onehourapp.utils.CalendarUtil
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

class NotificationService : Service() {
    private val notificationChannelId = "my_notification_channel"
    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Add Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return START_STICKY
    }

    private fun start() {
        val time = Calendar.getInstance().timeInMillis
        startForeground(time.toInt(),showNotification())
        startNotificationTimer()
    }


    private fun startNotificationTimer() {
        val timer = Timer()
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis

        val nextHour = CalendarUtil.getCurrentHour()
        calendar.set(Calendar.HOUR_OF_DAY, if(nextHour==23) 0 else nextHour + 1)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        var initialDelay = calendar.timeInMillis - currentTime
        if (initialDelay <= 0) {
            initialDelay += (24 * 60 * 60 * 1000)
        }
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val time = Calendar.getInstance().timeInMillis
                notificationManager?.notify(time.toInt(), showNotification())
            }
        }, 15*1000,  60 * 1000)
    }
    private fun scheduleNextNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextNotificationTime = System.currentTimeMillis() + (60 * 1000) // 1 hour
        val pendingIntent = PendingIntent.getService(
            this, 0, Intent(this, NotificationService::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextNotificationTime, pendingIntent)
    }

    private fun showNotification() : Notification{
        val currentTime = Calendar.getInstance().timeInMillis
        val notificationIntent = Intent(this, NotificationReceiver::class.java).putExtra("day",CalendarUtil.getCurrentDay()).putExtra("hour",CalendarUtil.getCurrentHour())
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Как прошел ваш час?")
            .setContentText("Нажмите, чтобы добавить запись")
            //.setContentText("Нажмите\n" + time)
            .setSmallIcon(R.drawable.ic_logo_small)
            .setContentIntent(pendingIntent)
            .setShowWhen(true)
            .setWhen(currentTime)
            .setAutoCancel(true)
            .setGroup("MY_NOTIFICATION_GROUP")
            .build()
        return notification

    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    enum class State{
        START,
        STOP
    }
}