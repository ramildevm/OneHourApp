package com.example.onehourapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.repositories.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repository: ActivityRepository
): ViewModel() {
    val insertResult = MutableLiveData<Int>()
    fun getActivities(id:Int) = repository.getActivitiesByCategoryId(id)
    fun getActivities() = repository.getActivities()

    fun getActivityById(activityId: Int): Activity = repository.getActivityById(activityId)

    fun insertActivity(activity: Activity){
        viewModelScope.launch{
            val id = repository.insertActivity(activity)
            insertResult.postValue(id.toInt())
        }
    }
    fun deleteActivity(activity: Activity){
        viewModelScope.launch{
            repository.deleteActivity(activity)
        }
    }

}