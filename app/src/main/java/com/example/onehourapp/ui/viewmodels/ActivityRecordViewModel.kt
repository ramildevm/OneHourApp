package com.example.onehourapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.data.repositories.ActivityRecordRepository
import com.example.onehourapp.data.repositories.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class ActivityRecordViewModel @Inject constructor(
    private val repository: ActivityRecordRepository
): ViewModel() {
    fun getActivityRecordsInInterval(startTime:Long, endTime:Long) = repository.getActivityRecordsByInterval(startTime,endTime)
    fun getActivityRecordsByYear(year:Int) = repository.getActivityRecordsByYear(year)
    fun getActivityRecordsByMonth(year:Int, month:Int) = repository.getActivityRecordsByMonth(year,month)
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