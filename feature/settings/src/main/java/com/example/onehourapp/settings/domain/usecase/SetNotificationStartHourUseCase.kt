package com.example.onehourapp.settings.domain.usecase

import com.example.onehourapp.settings.domain.repositories.NotificationRepository
import javax.inject.Inject

class SetNotificationStartHourUseCase @Inject constructor(
private val repository: NotificationRepository
) {
    suspend operator fun invoke(startHour:Int){
        repository.setNotificationStartHour(startHour)
    }
}