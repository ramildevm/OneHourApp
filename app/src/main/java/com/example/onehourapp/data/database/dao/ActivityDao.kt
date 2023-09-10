package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.Category
import kotlinx.coroutines.flow.Flow


@Dao
interface ActivityDao {
    @Query("SELECT * FROM Activity WHERE categoryId=:id")
    fun getActivitiesByCategoryId(id:Int): Flow<List<Activity>>

    @Upsert
    suspend fun insertUpdateActivity(activity: Activity)

    @Delete
    suspend fun deleteActivity(activity: Activity)
}