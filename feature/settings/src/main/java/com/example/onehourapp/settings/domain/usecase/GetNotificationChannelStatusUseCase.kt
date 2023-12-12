package com.example.onehourapp.settings.domain.usecase

import com.example.onehourapp.settings.domain.repositories.NotificationRepository
import javax.inject.Inject

class GetNotificationChannelStatusUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke():Boolean{
        return repository.getNotificationStatus()
    }
}

