package com.example.onehourapp.domain.usecase

import com.example.onehourapp.domain.repositories.UserSettingsRepository
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val repository: UserSettingsRepository
) {
    operator fun invoke() = repository.getUserSettings()
}