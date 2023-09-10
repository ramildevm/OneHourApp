package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.data.models.Category
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp


@Dao
interface ActivityRecordDao {
    @Query("SELECT * FROM ActivityRecord WHERE timestamp BETWEEN :startTimestamp AND :endTimeStamp")
    fun getActivityRecordsByInterval(startTimestamp:Long, endTimeStamp:Long): Flow<List<ActivityRecord>>

    @Query("SELECT * FROM ActivityRecord WHERE activityId = :id")
    fun getActivityRecordsByActivityId(id:Int): Flow<List<ActivityRecord>>

    @Query("SELECT * FROM ActivityRecord WHERE timestamp=:timestamp")
    fun getActivityRecordByTimeStamp(timestamp: Long) : ActivityRecord

    @Upsert
    suspend fun insertActivityRecord(activityRecord: ActivityRecord)

    @Delete
    suspend fun deleteActivityRecord(activityRecord: ActivityRecord)
}