package com.example.onehourapp.dialog.activity.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.common.ValidationEvent
import com.example.onehourapp.dialog.domain.InsertActivityUseCase
import com.example.onehourapp.dialog.domain.ValidateNameUseCase
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddActivityDialogViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val validateName: ValidateNameUseCase,
    private val insertActivity: InsertActivityUseCase
) :ViewModel(){
    val categories = getCategoriesUseCase.invoke().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )
    var state by mutableStateOf(AddActivityDialogState())
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()
    fun onEvent(event: AddActivityDialogEvent){
        when(event){
            is AddActivityDialogEvent.SelectedCategoryChanged -> {
                state = state.copy(
                    selectedCategory = event.category
                )
            }
            is AddActivityDialogEvent.NameChanged ->{
                state = state.copy(
                    name = event.name
                )
            }
            AddActivityDialogEvent.OnConfirm -> {
                val nameResult = validateName.invoke(state.name)
                if(!nameResult.successful){
                    state = state.copy(
                        nameError = nameResult.errorMessage
                    )
                    return
                }
                viewModelScope.launch {
                    insertActivity.invoke(Activity(
                        id = 0,
                        name = state.name,
                        categoryId = state.selectedCategory.id
                    ))
                    validationEventChannel.send(ValidationEvent.SUCCESS)
                }
            }
        }
    }

}
