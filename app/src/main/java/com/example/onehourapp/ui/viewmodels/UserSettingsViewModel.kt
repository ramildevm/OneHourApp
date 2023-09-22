package com.example.onehourapp.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.data.models.Category
import com.example.onehourapp.data.models.UserSettings
import com.example.onehourapp.data.repositories.CategoryRepository
import com.example.onehourapp.data.repositories.UserSettingsRepository
import com.example.onehourapp.helpers.SortingHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val repository: UserSettingsRepository
): ViewModel() {
    fun getUserSettings() = repository.getUserSettings()
    fun insertUserSettings(userSettings: UserSettings){
        viewModelScope.launch{
            repository.insertOrUpdateUserSettings(userSettings)
        }
    }
    fun updateUserSettingsAfterRecordAdd(userSettings: UserSettings){
        viewModelScope.launch{
            val oldUserSettings = repository.getUserSettings()
            repository.insertOrUpdateUserSettings(UserSettings(
                oldUserSettings.id,
                userSettings.lastAddedActivityId,
                userSettings.lastAddedDate,
                oldUserSettings.sleepActivityStartHour,
                oldUserSettings.sleepActivityEndHour
            ))
        }
    }
    fun updateUserSettingsAfterTimeSettingsChange(userSettings: UserSettings){
        viewModelScope.launch{
            val oldUserSettings = repository.getUserSettings()
            repository.insertOrUpdateUserSettings(UserSettings(
                oldUserSettings.id,
                oldUserSettings.lastAddedActivityId,
                oldUserSettings.lastAddedDate,
                userSettings.sleepActivityStartHour,
                userSettings.sleepActivityEndHour
            ))
        }
    }
}
