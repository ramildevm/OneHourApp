package com.example.onehourapp.glue.settings

import com.example.onehourapp.data.database.repositories.ActivityDataRepository
import com.example.onehourapp.data.database.repositories.UserSettingsDataRepository
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.UserSettings
import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.domain.repositories.UserSettingsRepository
import com.example.onehourapp.glue.activity.mappers.toEntity
import com.example.onehourapp.glue.activity.mappers.toModel
import com.example.onehourapp.glue.settings.mappers.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterUserSettingsRepository @Inject constructor(
    private val userSettingsDataRepository: UserSettingsDataRepository
) :UserSettingsRepository {

    override fun getUserSettings(): UserSettings {
        return userSettingsDataRepository.getUserSettings().toModel()
    }

    override suspend fun updateUserSettingsAddingData(lasAddedActivityId: Int, lastAddedDate: Long) {
        userSettingsDataRepository.updateUserSettingsAddingInfo(lasAddedActivityId,lastAddedDate)
    }
}