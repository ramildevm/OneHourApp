package com.example.onehourapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.onehourapp.data.ActivityDataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repository: ActivityDataRepositoryImpl
): ViewModel() {
    /*
    val insertResult = MutableLiveData<Int>()
    fun getActivities(id:Int) = repository.getActivitiesByCategoryId(id)
    fun getActivities() = repository.getActivities()
    fun getActivityById(activityId: Int): Activity? = repository.getActivityById(activityId)

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
    */
}