package com.example.onehourapp.data

import com.example.onehourapp.common.utils.CalendarUtil
import com.example.onehourapp.data.database.models.ActivityRecordEntity
import com.example.onehourapp.data.database.models.dto.CategoryCountEntity
import com.example.onehourapp.data.database.repositories.ActivityRecordDataRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Named

class ActivityRecordDataRepositoryImpl @Inject constructor(
    @Named("activityRecord") private val activityRecordDao: com.example.onehourapp.data.database.dao.ActivityRecordDao
) : ActivityRecordDataRepository
{
    override fun getActivityRecordsCountByActivity(activityId:Int) :Int{
        return activityRecordDao.getActivityRecordsCountByActivityId(activityId, 0L, Long.MAX_VALUE)
    }
    override fun getActivityRecordsCountByActivityInMonth(activityId:Int, year: Int, month: Int) :Int{
        val startOfMonth = CalendarUtil.getMonthStartMillis(year, month)
        val endOfMonth = CalendarUtil.getMonthStartMillis(year + if(month==11) 1 else 0,   if(month==11) 0 else month + 1)
        return activityRecordDao.getActivityRecordsCountByActivityId(activityId, startOfMonth, endOfMonth)
    }
    override fun getActivityRecordsCountByActivityInYear(activityId:Int, year: Int) :Int{
        val startOfYear = CalendarUtil.getYearStartMillis(year)
        val startOfNextYear = CalendarUtil.getYearStartMillis(year + 1)
        return activityRecordDao.getActivityRecordsCountByActivityId(activityId, startOfYear, startOfNextYear)
    }
    override fun getActivityRecordsByInterval(startTimestamp:Long, endTimeStamp:Long) :Flow<List<ActivityRecordEntity>>{
        return activityRecordDao.getActivityRecordsByInterval(startTimestamp,endTimeStamp)
    }
    override fun getActivityRecordsByYear(year: Int): Flow<List<ActivityRecordEntity>> {
        val startOfYear = CalendarUtil.getYearStartMillis(year)
        val startOfNextYear = CalendarUtil.getYearStartMillis(year+1)
        return activityRecordDao.getActivityRecordsByInterval(startOfYear,startOfNextYear)
    }
    override fun getActivityRecordsByMonth(year: Int, month: Int): Flow<List<ActivityRecordEntity>> {
        val startOfMonth = CalendarUtil.getMonthStartMillis(year, month)
        val endOfMonth = CalendarUtil.getMonthStartMillis(year + if(month==11) 1 else 0,   if(month==11) 0 else month + 1)
        return activityRecordDao.getActivityRecordsByInterval(startOfMonth,endOfMonth)
    }
    override fun getActivityRecordsByDay(year: Int, month: Int, day: Int): Flow<List<ActivityRecordEntity>> {
        val startOfDay = CalendarUtil.getDayStartMillis(year, month, day)
        val endOfDay = startOfDay + 82800000L
        return activityRecordDao.getActivityRecordsByInterval(startOfDay, endOfDay)
    }
    override fun getActivityRecordsCountByCategoryInMonth(categoryId:Int, year: Int, month: Int): Int {
        val startOfMonth = CalendarUtil.getMonthStartMillis(year, month)
        val endOfMonth = CalendarUtil.getMonthStartMillis(year + if(month==11) 1 else 0,   if(month==11) 0 else month + 1)
        return activityRecordDao.getActivityRecordsCountByCategoryId(categoryId, startOfMonth,endOfMonth)
    }
    override fun getActivityRecordsCountListByCategoriesInMonth(year: Int, month: Int): Flow<List<CategoryCountEntity>> {
        val startOfMonth = CalendarUtil.getMonthStartMillis(year, month)
        val endOfMonth = CalendarUtil.getMonthStartMillis(year + if(month==11) 1 else 0,   if(month==11) 0 else month + 1)
        return activityRecordDao.getActivityRecordsCountListByCategories(startOfMonth,endOfMonth)
    }
    override fun getActivityRecordsCountListByCategoriesInYear(year: Int): Flow<List<CategoryCountEntity>> {
        val startOfYear = CalendarUtil.getYearStartMillis(year)
        val startOfNextYear = CalendarUtil.getYearStartMillis(year+1)
        return activityRecordDao.getActivityRecordsCountListByCategories(startOfYear, startOfNextYear)
    }
    override fun getActivityRecordsCountByCategoryInDay(categoryId:Int, year: Int, month: Int, day: Int): Int {
        val startOfDay = CalendarUtil.getDayStartMillis(year, month, day)
        val endOfDay = startOfDay + 82800000L
        return activityRecordDao.getActivityRecordsCountByCategoryId(categoryId, startOfDay, endOfDay)
    }
    override fun getActivityRecordsForExcel() = activityRecordDao.getActivityRecordsForExcel()
    override fun getActivityRecords() = activityRecordDao.getActivityRecords()
    override fun getActivityRecordByTime(year: Int, month: Int, day:Int, hour:Int): ActivityRecordEntity? {
        val time = Calendar.getInstance()
        time.set(year,month,day,hour,0,0)
        time.set(Calendar.MILLISECOND, 0)
        return activityRecordDao.getActivityRecordByTimeStamp(time.timeInMillis)
    }

    override suspend fun insertOrUpdateActivityRecord(activityRecord: ActivityRecordEntity) {
        val oldActivityRecord = activityRecordDao.getActivityRecordByTimeStamp(activityRecord.timestamp)
        if(oldActivityRecord==null)
            activityRecordDao.insertActivityRecord(activityRecord)
        else
            activityRecordDao.insertActivityRecord(ActivityRecordEntity(oldActivityRecord.id,activityRecord.activityId, activityRecord.timestamp))
    }
    override suspend fun deleteActivityRecord(activityRecord: ActivityRecordEntity) = activityRecordDao.deleteActivityRecord(activityRecord)


}