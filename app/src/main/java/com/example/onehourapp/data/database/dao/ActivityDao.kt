package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.Category
import kotlinx.coroutines.flow.Flow


@Dao
interface ActivityDao {
    @Query("SELECT * FROM Activity WHERE categoryId=:id")
    fun getActivitiesByCategoryId(id:Int): Flow<List<Activity>>
    @Query("SELECT * FROM Activity")
    fun getActivities(): Flow<List<Activity>>
    @Query("SELECT * FROM Activity WHERE id=:id LIMIT 1")
    fun getActivityById(id: Int): Activity?

    @Insert
    suspend fun insertActivity(activity: Activity) :Long

    @Delete
    suspend fun deleteActivity(activity: Activity)
}