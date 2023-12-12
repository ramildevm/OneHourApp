package com.example.onehourapp.domain.repositories

import com.example.onehourapp.domain.models.UserSettings

interface UserSettingsRepository {
    fun getUserSettings():UserSettings
    suspend fun updateUserSettingsAddingData(lasAddedActivityId:Int, lastAddedDate:Long)
}