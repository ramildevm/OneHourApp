package com.example.onehourapp.data.database.repositories

import com.example.onehourapp.data.database.models.ActivityEntity
import kotlinx.coroutines.flow.Flow

interface ActivityDataRepository {

    fun getActivitiesByCategoryId(id: Int): Flow<List<ActivityEntity>>

    fun getActivities(): Flow<List<ActivityEntity>>

    fun getActivityById(activityId: Int): ActivityEntity?

    suspend fun insertActivity(activity: ActivityEntity): Long

    suspend fun deleteActivity(activityId: Int)
}