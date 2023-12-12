package com.example.onehourapp.domain.repositories

import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.ActivityRecord
import com.example.onehourapp.domain.models.Category
import kotlinx.coroutines.flow.Flow

interface ActivityRecordRepository {
    suspend fun insertActivityRecord(activityRecord: ActivityRecord)
}