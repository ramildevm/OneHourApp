package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
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
}