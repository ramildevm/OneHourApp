package com.example.onehourapp.domain.repositories

import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.Category
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivitiesByCategoryId(categoryId:Int): Flow<List<Activity>>
    fun getActivityById(activityId:Int): Activity?
    suspend fun insertActivity(activity:Activity):Int
    suspend fun deleteActivityById(activityId: Int)
}