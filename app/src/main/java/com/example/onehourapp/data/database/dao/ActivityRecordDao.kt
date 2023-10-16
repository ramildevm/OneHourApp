package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.data.models.Category
import com.example.onehourapp.data.models.dto.CategoryCount
import com.example.onehourapp.data.models.dto.ExcelRecord
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
    @Query("SELECT c.id as id, c.name as name, c.color as color, COUNT(*) as count FROM ActivityRecord ar " +
            "INNER JOIN Activity a ON ar.activityId = a.id " +
            "INNER JOIN Category c ON c.id = a.categoryId " +
            "WHERE ar.timestamp BETWEEN :startTimestamp AND :endTimeStamp " +
            "GROUP BY a.categoryId ")
    fun getActivityRecordsCountListByCategories(startTimestamp: Long, endTimeStamp: Long): Flow<List<CategoryCount>>
    @Query("SELECT ar.id as id, c.name as category, a.name as activity, c.color as color, ar.timestamp as timestamp FROM ActivityRecord ar " +
            "INNER JOIN Activity a ON ar.activityId = a.id " +
            "INNER JOIN Category c ON c.id = a.categoryId " +
            "ORDER BY timestamp ASC")
    fun getActivityRecordsForExcel(): Flow<List<ExcelRecord>>
    @Upsert
    suspend fun insertActivityRecord(activityRecord: ActivityRecord)

    @Delete
    suspend fun deleteActivityRecord(activityRecord: ActivityRecord)
}