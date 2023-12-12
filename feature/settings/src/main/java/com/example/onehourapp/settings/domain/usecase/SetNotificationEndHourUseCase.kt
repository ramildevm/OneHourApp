package com.example.onehourapp.settings.domain.usecase

import com.example.onehourapp.settings.domain.repositories.NotificationRepository
import javax.inject.Inject

class SetNotificationEndHourUseCase @Inject constructor(
private val repository: NotificationRepository
) {
    suspend operator fun invoke(endHour:Int){
        repository.setNotificationEndHour(endHour)
    }
}