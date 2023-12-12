package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.onehourapp.data.database.models.UserSettingsEntity

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM UserSettings WHERE id=1")
    fun getUserSettings(): UserSettingsEntity
    @Upsert
    suspend fun insertUpdateUserSettings(settings: UserSettingsEntity)
    @Query("UPDATE UserSettings " +
            "SET notificationStartHour=:sleepStartHour " +
            "WHERE id = 1;")
    suspend fun updateUserSettingsNotificationStart(sleepStartHour:Int)
    @Query("UPDATE UserSettings " +
            "SET notificationEndHour=:sleepEndHour " +
            "WHERE id = 1;")
    suspend fun updateUserSettingsNotificationEnd(sleepEndHour:Int)
    @Query("UPDATE UserSettings " +
            "SET lastAddedActivityId=:activityId, " +
            "lastAddedDate=:date " +
            "WHERE id = 1;")
    suspend fun updateUserSettingsAddingInfo(activityId:Int, date:Long)
    @Query("UPDATE UserSettings " +
            "SET lastAddedDate=:date " +
            "WHERE id = 1;")
    suspend fun updateUserSettingsAddedDate(date:Long)
}