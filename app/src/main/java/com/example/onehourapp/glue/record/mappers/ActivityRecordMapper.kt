package com.example.onehourapp.glue.record.mappers

import com.example.onehourapp.data.database.models.ActivityRecordEntity
import com.example.onehourapp.domain.models.ActivityRecord

fun ActivityRecord.toEntity():ActivityRecordEntity{
    return ActivityRecordEntity(
        id = this.id,
        activityId = this.activityId,
        timestamp = this.timestamp
    )
}
fun ActivityRecordEntity.toModel():ActivityRecord{
    return ActivityRecord(
        id = this.id,
        activityId = this.activityId,
        timestamp = this.timestamp
    )
}