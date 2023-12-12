package com.example.onehourapp.dialog.record.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.dialog.domain.InsertActivityUseCase
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.ActivityRecord
import com.example.onehourapp.domain.models.UserSettings
import com.example.onehourapp.domain.usecase.GetActivitiesByCategoryUseCase
import com.example.onehourapp.domain.usecase.GetActivityByIdUseCase
import com.example.onehourapp.domain.usecase.GetCategoriesUseCase
import com.example.onehourapp.domain.usecase.GetUserSettingsUseCase
import com.example.onehourapp.domain.usecase.InsertActivityRecordUseCase
import com.example.onehourapp.domain.usecase.UpdateUserSettingsAddingDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddRecordDialogViewModel @Inject constructor(
    private val getUserSettings: GetUserSettingsUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val getActivitiesByCategory: GetActivitiesByCategoryUseCase,
    private val getActivityById: GetActivityByIdUseCase,
    private val updateUserSettingsAddingData: UpdateUserSettingsAddingDataUseCase,
    private val insertActivityRecord: InsertActivityRecordUseCase,
    private val insertActivity: InsertActivityUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AddRecordDialogState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), AddRecordDialogState())
    private val insertRecordsEventChannel = Channel<InsertRecordsResult>()
    val insertRecordsEvents = insertRecordsEventChannel.receiveAsFlow()
    fun getUserSettings(): UserSettings = getUserSettings.invoke()
    fun getCategories() = getCategories.invoke()
    fun getActivitiesByCategoryId(categoryId: Int) = getActivitiesByCategory.invoke(categoryId)
    fun getActivityById(activityId: Int): Activity? = getActivityById.invoke(activityId)
    private fun insertActivityRecord(activityRecord: ActivityRecord) {
        viewModelScope.launch {
            insertActivityRecord.invoke(activityRecord)
        }
    }
    fun onEvent(event:AddRecordDialogEvent){
        when(event){
            is AddRecordDialogEvent.OnAdd ->{
                viewModelScope.launch{
                    if(event.activity!=null){
                        state.value.selectedActivityId = insertActivity.invoke(event.activity)
                    }
                    createActivityRecord(
                        selectedActivityId = state.value.selectedActivityId,
                        selectedDateMillis = event.date,
                        startHour = event.startHour,
                        endHour = event.endHour
                    )
                }
            }
            is AddRecordDialogEvent.SelectedActivityIdChanged -> {
                _state.update {
                    it.copy(
                        selectedActivityId = event.id
                    )
                }
            }
        }
    }
    private suspend fun createActivityRecord(
        selectedDateMillis: Long,
        startHour: Int,
        endHour: Int,
        selectedActivityId: Int
    ) {
        val startDate = Calendar.getInstance()
        startDate.timeInMillis = selectedDateMillis
        startDate.set(Calendar.HOUR_OF_DAY, startHour)
        startDate.set(Calendar.MINUTE, 0)
        startDate.set(Calendar.SECOND, 0)
        startDate.set(Calendar.MILLISECOND, 0)

        val endDate = Calendar.getInstance()
        endDate.timeInMillis = selectedDateMillis + if (endHour == 24) com.example.onehourapp.common.utils.CalendarUtil.DAY_MILLIS else 0L
        endDate.set(Calendar.HOUR_OF_DAY, if (endHour == 24) 0 else endHour)
        endDate.set(Calendar.MINUTE, 0)
        endDate.set(Calendar.SECOND, 0)
        endDate.set(Calendar.MILLISECOND, 0)

        for (timestamp in startDate.timeInMillis until endDate.timeInMillis step com.example.onehourapp.common.utils.CalendarUtil.HOUR_MILLIS) {
            insertActivityRecord(
                ActivityRecord(
                    0,
                    selectedActivityId,
                    timestamp
                )
            )
        }
        updateUserSettingsAddingData(selectedActivityId, selectedDateMillis)
        insertRecordsEventChannel.send(InsertRecordsResult.SUCCESS)
    }
    enum class InsertRecordsResult{
        SUCCESS
    }
}
