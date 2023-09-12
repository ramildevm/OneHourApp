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
    fun getActivities(id:Int) = repository.getActivitiesByCategoryId(id)
    
    fun insertActivity(activity: Activity){
        viewModelScope.launch{
            repository.insertOrUpdateActivity(activity)
        }
    }
    fun updateActivity(activity: Activity){
        viewModelScope.launch{
            repository.insertOrUpdateActivity(activity)
        }
    }
    fun deleteActivity(activity: Activity){
        viewModelScope.launch{
            repository.deleteActivity(activity)
        }
    }
}