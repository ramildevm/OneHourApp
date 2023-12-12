package com.example.onehourapp.data.database.repositories

import com.example.onehourapp.data.database.models.ActivityRecordEntity
import com.example.onehourapp.data.database.models.dto.CategoryCountEntity
import com.example.onehourapp.data.database.models.dto.ExcelRecordEntity
import kotlinx.coroutines.flow.Flow

interface ActivityRecordDataRepository {

    fun getActivityRecordsCountByActivity(activityId: Int): Int

    fun getActivityRecordsCountByActivityInMonth(activityId: Int, year: Int, month: Int): Int

    fun getActivityRecordsCountByActivityInYear(activityId: Int, year: Int): Int

    fun getActivityRecordsByInterval(startTimestamp: Long, endTimeStamp: Long): Flow<List<ActivityRecordEntity>>

    fun getActivityRecordsByYear(year: Int): Flow<List<ActivityRecordEntity>>

    fun getActivityRecordsByMonth(year: Int, month: Int): Flow<List<ActivityRecordEntity>>

    fun getActivityRecordsByDay(year: Int, month: Int, day: Int): Flow<List<ActivityRecordEntity>>

    fun getActivityRecordsCountByCategoryInMonth(categoryId: Int, year: Int, month: Int): Int

    fun getActivityRecordsCountListByCategoriesInMonth(year: Int, month: Int): Flow<List<CategoryCountEntity>>

    fun getActivityRecordsCountListByCategoriesInYear(year: Int): Flow<List<CategoryCountEntity>>

    fun getActivityRecordsCountByCategoryInDay(categoryId: Int, year: Int, month: Int, day: Int): Int

    fun getActivityRecordsForExcel(): Flow<List<ExcelRecordEntity>>

    fun getActivityRecords(): Flow<List<ActivityRecordEntity>>

    fun getActivityRecordByTime(year: Int, month: Int, day: Int, hour: Int): ActivityRecordEntity?

    suspend fun insertOrUpdateActivityRecord(activityRecord: ActivityRecordEntity)

    suspend fun deleteActivityRecord(activityRecord: ActivityRecordEntity)
}