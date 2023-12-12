package com.example.onehourapp.domain.usecase

import com.example.onehourapp.domain.repositories.UserSettingsRepository
import javax.inject.Inject

class UpdateUserSettingsAddingDataUseCase @Inject constructor(
    private val repository: UserSettingsRepository
) {
    suspend operator fun invoke(lasAddedActivityId:Int, lastAddedDate:Long) = repository.updateUserSettingsAddingData(lasAddedActivityId, lastAddedDate)
}