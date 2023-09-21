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
    @Query("SELECT COUNT(*) FROM ActivityRecord WHERE activityId = :id and timestamp between :startTimestamp and :endTimeStamp")
    fun getActivityRecordsCountByActivityId(id:Int, startTimestamp: Long, endTimeStamp: Long): Int

    @Query("SELECT * FROM ActivityRecord WHERE timestamp=:timestamp")
    fun getActivityRecordByTimeStamp(timestamp: Long) : ActivityRecord?
    @Query("SELECT COUNT(*) FROM ActivityRecord ar " +
            "INNER JOIN Activity a ON ar.activityId = a.id " +
            "WHERE a.categoryId = :categoryId " +
            "AND ar.timestamp BETWEEN :startTimestamp AND :endTimeStamp")
    fun getActivityRecordsCountByCategoryId(categoryId: Int, startTimestamp: Long, endTimeStamp: Long): Int
    @Upsert
    suspend fun insertActivityRecord(activityRecord: ActivityRecord)

    @Delete
    suspend fun deleteActivityRecord(activityRecord: ActivityRecord)
}