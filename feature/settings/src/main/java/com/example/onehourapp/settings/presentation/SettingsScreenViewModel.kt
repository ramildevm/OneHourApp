package com.example.onehourapp.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.settings.domain.usecase.GetNotificationChannelStatusUseCase
import com.example.onehourapp.settings.domain.usecase.GetNotificationEndHourUseCase
import com.example.onehourapp.settings.domain.usecase.GetNotificationStartHourUseCase
import com.example.onehourapp.settings.domain.usecase.SetNotificationChannelStatusUseCase
import com.example.onehourapp.settings.domain.usecase.SetNotificationEndHourUseCase
import com.example.onehourapp.settings.domain.usecase.SetNotificationStartHourUseCase
import com.example.onehourapp.settings.domain.usecase.SyncDataWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val getNotificationChannelStatus: GetNotificationChannelStatusUseCase,
    private val setNotificationChannelStatus: SetNotificationChannelStatusUseCase,
    private val getNotificationStartHour: GetNotificationStartHourUseCase,
    private val setNotificationStartHour: SetNotificationStartHourUseCase,
    private val getNotificationEndHour: GetNotificationEndHourUseCase,
    private val setNotificationEndHour: SetNotificationEndHourUseCase,
    private val syncDataWithGoogle: SyncDataWithGoogleUseCase
) : ViewModel(){
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    private val _state = MutableStateFlow(SettingsScreenState())
    var state = _state.apply {
        value = value.copy(
            notificationOnCheck = getNotificationChannelStatus.invoke(),
            notificationStartHour = getNotificationStartHour.invoke(),
            notificationEndHour = getNotificationEndHour.invoke(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsScreenState())

    fun onEvent(event:SettingsScreenEvent){
        when(event){
            is SettingsScreenEvent.EndHourChanged -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            notificationEndHour = event.value
                        )
                    }
                    setNotificationEndHour.invoke(event.value)
                }
            }
            is SettingsScreenEvent.StartHourChanged -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            notificationStartHour = event.value
                        )
                    }
                    setNotificationStartHour.invoke(event.value)
                }
            }
            is SettingsScreenEvent.StatusChanged -> {
                setNotificationChannelStatus.invoke(event.isEnabled)
                _state.update {
                    it.copy(
                        notificationOnCheck = event.isEnabled
                    )
                }
            }
            SettingsScreenEvent.SyncWithGoogle -> {
                viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                    _state.value = _state.value.copy(
                        googleResponseResult = syncDataWithGoogle()
                    )
                }
            }
        }
    }
}
