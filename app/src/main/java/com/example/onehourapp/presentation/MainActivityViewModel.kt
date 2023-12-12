package com.example.onehourapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.domain.usecase.UpdateUserSettingsAddingDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val updateUserSettingsAddingData: UpdateUserSettingsAddingDataUseCase
) :ViewModel() {
    fun updateUserSettingsAddingDate(lastAddedDate:Long) {
        viewModelScope.launch {
            updateUserSettingsAddingData(-1, lastAddedDate)
        }
    }
}

