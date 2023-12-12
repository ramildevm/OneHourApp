package com.example.onehourapp.domain.usecase

import com.example.onehourapp.domain.models.ActivityRecord
import com.example.onehourapp.domain.repositories.ActivityRecordRepository
import javax.inject.Inject

class InsertActivityRecordUseCase @Inject constructor(
    private val repository: ActivityRecordRepository
) {
    suspend operator fun invoke(activityRecord:ActivityRecord){
        repository.insertActivityRecord(activityRecord)
    }
}