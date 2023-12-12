package com.example.onehourapp.notification.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.onehourapp.NOTIFICATION_CURRENT_TIME
import com.example.onehourapp.data.database.di.ApplicationScope
import com.example.onehourapp.domain.models.ActivityRecord
import com.example.onehourapp.domain.usecase.GetUserSettingsUseCase
import com.example.onehourapp.domain.usecase.InsertActivityRecordUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class NotificationActionReceiver: BroadcastReceiver() {
    @Inject
    lateinit var insertActivityRecord: InsertActivityRecordUseCase
    @Inject
    lateinit var getUserSettings: GetUserSettingsUseCase
    @Inject
    @ApplicationScope
    lateinit var scope: CoroutineScope
    override fun onReceive(context: Context?, intent: Intent?) {
        val extraData = intent?.extras?.getLong(NOTIFICATION_CURRENT_TIME, 0L)
        val id = intent?.extras?.getInt(idExtra, -1)
        if(extraData!=null && extraData!=0L){
            scope.launch {
                val userSettings = getUserSettings()
                val timestamp = extraData - 60 * 60 * 1000
                insertActivityRecord(
                    ActivityRecord(
                        0,
                        userSettings.lastAddedActivityId,
                        timestamp
                    )
                )
                (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)?.cancel(id?:-1)

            }
        }
    }
}