package com.example.onehourapp.settings.domain.usecase

import com.example.onehourapp.settings.domain.repositories.NotificationRepository
import javax.inject.Inject

class GetNotificationStartHourUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke():Int{
        return repository.getNotificationStartHour()
    }
}

