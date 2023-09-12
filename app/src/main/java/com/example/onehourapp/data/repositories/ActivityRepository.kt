package com.example.onehourapp.data.repositories

import com.example.onehourapp.data.database.dao.ActivityDao
import com.example.onehourapp.data.database.dao.ActivityRecordDao
import com.example.onehourapp.data.models.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import javax.inject.Inject
import javax.inject.Named

class ActivityRepository @Inject constructor(
    @Named("activity") private val activityDao: ActivityDao,
    @Named("activityRecord") private val activityRecordDao: ActivityRecordDao
){
    fun getActivitiesByCategoryId(id:Int): Flow<List<Activity>> {
        return activityDao.getActivitiesByCategoryId(id)
    }
    suspend fun insertOrUpdateActivity(activity: Activity){
        activityDao.insertUpdateActivity(activity)
    }
    suspend fun deleteActivity(activity: Activity) {
        activityDao.deleteActivity(activity)
    }
}