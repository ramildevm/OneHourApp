package com.example.onehourapp.data.repositories

import com.example.onehourapp.data.database.dao.UserSettingsDao
import com.example.onehourapp.data.models.UserSettings
import javax.inject.Inject
import javax.inject.Named

class UserSettingsRepository @Inject constructor(
    @Named("userSettings") private val userSettingsDao: UserSettingsDao
){
    fun getUserSettings() = userSettingsDao.getUserSettings()
    suspend fun insertOrUpdateUserSettings(userSettings: UserSettings) = userSettingsDao.insertUpdateUserSettings(userSettings)
    suspend fun updateUserSettingsAddingInfo(activityId:Int, date:Long) = userSettingsDao.updateUserSettingsAddingInfo(activityId, date)
    suspend fun updateUserSettingsNotificationData(notificationStartHour:Int? = null, notificationEndHour:Int? = null) {
        if(notificationStartHour!=null)
            userSettingsDao.updateUserSettingsNotificationStart(notificationStartHour)
        if(notificationEndHour!=null)
            userSettingsDao.updateUserSettingsNotificationEnd(notificationEndHour)
    }
}