package com.example.onehourapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.data.models.UserSettings
import com.example.onehourapp.data.repositories.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    fun updateUserSettingsAddingData(lasAddedActivityId:Int, lastAddedDate:Long){
        viewModelScope.launch{
            repository.updateUserSettingsAddingInfo(lasAddedActivityId, lastAddedDate)
        }
    }
    fun updateUserSettingsNotificationSleepStart(sleepStartHour:Int){
        viewModelScope.launch{
            repository.updateUserSettingsNotificationData(notificationStartHour = sleepStartHour)
        }
    }
    fun updateUserSettingsNotificationSleepEnd(sleepEndHour:Int){
        viewModelScope.launch{
            repository.updateUserSettingsNotificationData(notificationEndHour = sleepEndHour)
        }
    }
}
