package com.example.onehourapp.data.repositories

import com.example.onehourapp.data.database.dao.ActivityRecordDao
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.utils.CalendarUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import java.time.Month
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Named

class ActivityRecordRepository @Inject constructor(
    @Named("activityRecord") private val activityRecordDao: ActivityRecordDao
){
    fun getActivityRecordsCountByActivity(activityId:Int) :Int{
        return activityRecordDao.getActivityRecordsCountByActivityId(activityId)
    }
    fun getActivityRecordsByInterval(startTimestamp:Long, endTimeStamp:Long) :Flow<List<ActivityRecord>>{
        return activityRecordDao.getActivityRecordsByInterval(startTimestamp,endTimeStamp)
    }
    fun getActivityRecordsByYear(year: Int): Flow<List<ActivityRecord>> {
        val startOfYear = CalendarUtil.getYearStartMillis(year)
        val startOfNextYear = CalendarUtil.getYearStartMillis(year+1)
        return activityRecordDao.getActivityRecordsByInterval(startOfYear,startOfNextYear)
    }
    fun getActivityRecordsByMonth(year: Int, month: Int): Flow<List<ActivityRecord>> {
        val startOfMonth = CalendarUtil.getMonthStartMillis(year, month)
        val endOfMonth = CalendarUtil.getMonthStartMillis(year, month)
        return activityRecordDao.getActivityRecordsByInterval(startOfMonth,endOfMonth)
    }
    fun getActivityRecordByTime(year: Int, month: Int, day:Int, hour:Int): ActivityRecord {
        val time = Calendar.getInstance()
        time.set(year,month,day,hour,0,0)
        time.set(Calendar.MILLISECOND, 0)
        return activityRecordDao.getActivityRecordByTimeStamp(time.timeInMillis)!!
    }

    suspend fun insertOrUpdateActivityRecord(activityRecord: ActivityRecord) {
        val oldActivityRecord = activityRecordDao.getActivityRecordByTimeStamp(activityRecord.timestamp)
        if(oldActivityRecord==null)
            activityRecordDao.insertActivityRecord(activityRecord)
        else
            activityRecordDao.insertActivityRecord(ActivityRecord(oldActivityRecord.id,activityRecord.activityId,activityRecord.timestamp))
    }
    suspend fun deleteActivityRecord(activityRecord: ActivityRecord) = activityRecordDao.deleteActivityRecord(activityRecord)

}