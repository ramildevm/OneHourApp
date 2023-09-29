package com.example.onehourapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.data.repositories.ActivityRecordRepository
import com.example.onehourapp.data.repositories.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class ActivityRecordViewModel @Inject constructor(
    private val repository: ActivityRecordRepository
): ViewModel() {
    fun getActivityRecordsByYear(year:Int) = repository.getActivityRecordsByYear(year)
    fun getActivityRecordsByMonth(year:Int, month:Int) = repository.getActivityRecordsByMonth(year,month)
    fun getActivityRecordByDay(year:Int, month:Int, day: Int) = repository.getActivityRecordsByDay(year, month, day)
    fun getActivityRecordsCountByActivityId(activityId:Int) : Int {
         return repository.getActivityRecordsCountByActivity(activityId)
    }
    fun getActivityRecordsCountByActivityInMonth(activityId:Int, year:Int, month:Int) = repository.getActivityRecordsCountByActivityInMonth(activityId, year, month)
    fun getActivityRecordsCountByActivityInYear(activityId:Int, year:Int) = repository.getActivityRecordsCountByActivityInYear(activityId, year)
    fun getActivityRecordsCountByCategoryInMonth(categoryId:Int, year:Int, month:Int) = repository.getActivityRecordsCountByCategoryInMonth(categoryId,year,month)
    fun getActivityRecordsCountListByCategoriesInMonth(year:Int, month:Int) = repository.getActivityRecordsCountListByCategoriesInMonth(year,month)
    fun getActivityRecordsCountListByCategoriesInYear(year:Int) = repository.getActivityRecordsCountListByCategoriesInYear(year)
    fun getActivityRecordsCountByCategoryInDay(categoryId:Int, year:Int, month:Int, day:Int) = repository.getActivityRecordsCountByCategoryInDay(categoryId,year,month, day)
    fun getActivityRecordByTime(year:Int, month:Int, day: Int,hour:Int) = repository.getActivityRecordByTime(year,month,day,hour)

    fun insertActivityRecord(activityRecord: ActivityRecord){
        viewModelScope.launch{
            repository.insertOrUpdateActivityRecord(activityRecord)
        }
    }
    fun updateActivityRecord(activityRecord: ActivityRecord){
        viewModelScope.launch{
            repository.insertOrUpdateActivityRecord(activityRecord)
        }
    }
    fun deleteActivityRecord(activityRecord: ActivityRecord){
        viewModelScope.launch{
            repository.deleteActivityRecord(activityRecord)
        }
    }
}