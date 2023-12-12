package com.example.onehourapp.glue.settings

import android.content.Context
import android.util.Log
import com.example.onehourapp.data.database.repositories.ActivityDataRepository
import com.example.onehourapp.data.database.repositories.UserSettingsDataRepository
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.glue.activity.mappers.toEntity
import com.example.onehourapp.glue.activity.mappers.toModel
import com.example.onehourapp.notification.NotificationChannelBuilder
import com.example.onehourapp.settings.domain.repositories.NotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterNotificationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userSettingsDataRepository: UserSettingsDataRepository
) : NotificationRepository {
    override fun getNotificationStatus(): Boolean {
        return NotificationChannelBuilder.isNotificationChannelEnabled(context)
    }

    override fun setNotificationStatus(enabled: Boolean) {
        if(!enabled){
            NotificationChannelBuilder.deleteNotificationChannel(context)
        }
        else{
            NotificationChannelBuilder.createNotificationChannel(context)
        }
    }

    override fun getNotificationStartHour(): Int {
        return userSettingsDataRepository.getUserSettings().notificationStartHour
    }

    override suspend fun setNotificationStartHour(startHour: Int) {
        userSettingsDataRepository.updateUserSettingsNotificationStartHour(startHour)
    }

    override fun getNotificationEndHour(): Int {
        return userSettingsDataRepository.getUserSettings().notificationEndHour
    }

    override suspend fun setNotificationEndHour(endHour: Int) {
        userSettingsDataRepository.updateUserSettingsNotificationEndHour(endHour)
    }

}