package com.example.onehourapp.data

import com.example.onehourapp.data.database.models.ActivityEntity
import com.example.onehourapp.data.database.repositories.ActivityDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class ActivityDataRepositoryImpl @Inject constructor(
    @Named("activity") private val activityDao: com.example.onehourapp.data.database.dao.ActivityDao,
    @Named("activityRecord") private val activityRecordDao: com.example.onehourapp.data.database.dao.ActivityRecordDao
) :ActivityDataRepository {
    override fun getActivitiesByCategoryId(id:Int): Flow<List<ActivityEntity>> {
        return activityDao.getActivitiesByCategoryId(id)
    }
    override fun getActivities(): Flow<List<ActivityEntity>> {
        return activityDao.getActivities()
    }
    override fun getActivityById(activityId: Int): ActivityEntity? {
        return activityDao.getActivityById(activityId)
    }
    override suspend fun insertActivity(activity: ActivityEntity) : Long {
        return activityDao.insertActivity(activity)
    }
    override suspend fun deleteActivity(activityId: Int) {
        val activity = activityDao.getActivityById(activityId)
        activity?.let {
            activityDao.deleteActivity(activity)
        }
    }
}