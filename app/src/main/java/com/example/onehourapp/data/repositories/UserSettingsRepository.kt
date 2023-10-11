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
    suspend fun updateUserSettingsNotificationData(isNotificationsEnable:Boolean? = null, sleepStartHour:Int? = null, sleepEndHour:Int? = null) {
        if(isNotificationsEnable!=null)
            userSettingsDao.updateUserSettingsNotificationStatus(isNotificationsEnable)
        if(sleepStartHour!=null)
            userSettingsDao.updateUserSettingsNotificationStart(sleepStartHour)
        if(sleepEndHour!=null)
            userSettingsDao.updateUserSettingsNotificationEnd(sleepEndHour)
    }
}