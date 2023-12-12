package com.example.onehourapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.onehourapp.data.UserSettingsDataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val repository: UserSettingsDataRepositoryImpl
): ViewModel() {
    /*
    fun getUserSettings() = repository.getUserSettings()
    fun insertUserSettings(userSettings: UserSettings){
        viewModelScope.launch{
            repository.insertOrUpdateUserSettings(userSettings)
        }
    }
    fun updateUserSettingsAddingData(lasAddedActivityId:Int = -1, lastAddedDate:Long){
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

     */
}
