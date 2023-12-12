package com.example.onehourapp.glue.activity

import com.example.onehourapp.data.database.repositories.ActivityDataRepository
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.glue.activity.mappers.toEntity
import com.example.onehourapp.glue.activity.mappers.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterActivityRepository @Inject constructor(
    private val activityDataRepository: ActivityDataRepository
) :ActivityRepository {
    override fun getActivitiesByCategoryId(categoryId: Int): Flow<List<Activity>> {
        return activityDataRepository.getActivitiesByCategoryId(categoryId).map { list->
            list.map {
                it.toModel()
            }
        }
    }

    override fun getActivityById(activityId: Int): Activity? {
        return activityDataRepository.getActivityById(activityId)?.toModel()
    }

    override suspend fun insertActivity(activity: Activity): Int {
        return activityDataRepository.insertActivity(activity.toEntity()).toInt()
    }

    override suspend fun deleteActivityById(activityId: Int) {
        activityDataRepository.deleteActivity(activityId)
    }
}