package com.example.onehourapp.dialog.domain

import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.repositories.ActivityRepository
import javax.inject.Inject

class InsertActivityUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(activity: Activity) = repository.insertActivity(activity)

}