package com.example.onehourapp.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.onehourapp.data.database.dao.ActivityRecordDao
import com.example.onehourapp.data.database.dao.UserSettingsDao
import com.example.onehourapp.data.di.ApplicationScope
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.utils.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {
    @Inject
    @Named("userSettings")
    lateinit var userSettingsDao: UserSettingsDao

    @Inject
    @Named("activityRecord")
    lateinit var activityRecordDao: ActivityRecordDao

    @Inject
    @ApplicationScope
    lateinit var scope: CoroutineScope
    override fun onReceive(context: Context?, intent: Intent?) {
        val extraData = intent?.extras?.getLong(currentTime, 0L)
        val id = intent?.extras?.getInt(idExtra, -1)
        if(extraData!=null && extraData!=0L){
            scope.launch {
                val userSettings = userSettingsDao.getUserSettings()
                val timestamp = extraData - 60 * 60 * 1000
                val oldActivityRecordId = activityRecordDao.getActivityRecordByTimeStamp(timestamp)?.id?:0
                activityRecordDao.insertActivityRecord(
                    ActivityRecord(
                        oldActivityRecordId,
                        userSettings.lastAddedActivityId,
                        timestamp
                    )
                )
                (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)?.cancel(id?:-1)

            }
        }
    }
}