package com.example.onehourapp.data.database.repositories

import com.example.onehourapp.data.database.models.UserSettingsEntity

interface UserSettingsDataRepository {

    fun getUserSettings(): UserSettingsEntity

    suspend fun insertOrUpdateUserSettings(userSettings: UserSettingsEntity)

    suspend fun updateUserSettingsAddingInfo(activityId: Int, date: Long)

    suspend fun updateUserSettingsNotificationEndHour(notificationEndHour:Int)
    suspend fun updateUserSettingsNotificationStartHour(notificationStartHour:Int)
}
