package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM UserSettings WHERE id=1")
    fun getUserSettings(): UserSettings
    @Upsert
    suspend fun insertUpdateUserSettings(settings: UserSettings)
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
}