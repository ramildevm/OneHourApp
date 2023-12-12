package com.example.onehourapp.activities.domain.usecase

import com.example.onehourapp.domain.repositories.ActivityRepository
import javax.inject.Inject

class DeleteActivityUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(activityId: Int) = repository.deleteActivityById(activityId)
}