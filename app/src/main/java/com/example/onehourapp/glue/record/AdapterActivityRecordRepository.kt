package com.example.onehourapp.glue.record

import com.example.onehourapp.data.database.repositories.ActivityDataRepository
import com.example.onehourapp.data.database.repositories.ActivityRecordDataRepository
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.ActivityRecord
import com.example.onehourapp.domain.repositories.ActivityRecordRepository
import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.glue.activity.mappers.toEntity
import com.example.onehourapp.glue.activity.mappers.toModel
import com.example.onehourapp.glue.record.mappers.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterActivityRecordRepository @Inject constructor(
    private val activityRecordDataRepository: ActivityRecordDataRepository
) :ActivityRecordRepository {
    override suspend fun insertActivityRecord(activityRecord: ActivityRecord) {
        activityRecordDataRepository.insertOrUpdateActivityRecord(activityRecord.toEntity())
    }
}