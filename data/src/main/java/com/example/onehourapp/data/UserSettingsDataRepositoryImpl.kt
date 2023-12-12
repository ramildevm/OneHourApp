package com.example.onehourapp.data

import com.example.onehourapp.data.database.models.UserSettingsEntity
import com.example.onehourapp.data.database.repositories.UserSettingsDataRepository
import javax.inject.Inject
import javax.inject.Named

class UserSettingsDataRepositoryImpl @Inject constructor(
    @Named("userSettings") private val userSettingsDao: com.example.onehourapp.data.database.dao.UserSettingsDao
) :UserSettingsDataRepository {
    override fun getUserSettings() = userSettingsDao.getUserSettings()
    override suspend fun insertOrUpdateUserSettings(userSettings: UserSettingsEntity) = userSettingsDao.insertUpdateUserSettings(userSettings)
    override suspend fun updateUserSettingsAddingInfo(activityId:Int, date:Long){
        if(activityId!=-1)
            userSettingsDao.updateUserSettingsAddingInfo(activityId, date)
        else
            userSettingsDao.updateUserSettingsAddedDate(date)

    }

    override suspend fun updateUserSettingsNotificationEndHour(notificationEndHour: Int) {
        userSettingsDao.updateUserSettingsNotificationEnd(notificationEndHour)
    }

    override suspend fun updateUserSettingsNotificationStartHour(notificationStartHour: Int) {
        userSettingsDao.updateUserSettingsNotificationStart(notificationStartHour)
    }
}