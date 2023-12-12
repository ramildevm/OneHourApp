package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.onehourapp.data.database.models.ActivityEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ActivityDao {
    @Query("SELECT * FROM Activity WHERE categoryId=:id")
    fun getActivitiesByCategoryId(id:Int): Flow<List<ActivityEntity>>
    @Query("SELECT * FROM Activity")
    fun getActivities(): Flow<List<ActivityEntity>>
    @Query("SELECT * FROM Activity WHERE id=:id LIMIT 1")
    fun getActivityById(id: Int): ActivityEntity?

    @Upsert
    suspend fun insertActivity(activity: ActivityEntity) :Long

    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)
}