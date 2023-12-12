package com.example.onehourapp.dialog.category.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.common.ValidationEvent
import com.example.onehourapp.dialog.category.domain.InsertCategoryUseCase
import com.example.onehourapp.dialog.domain.InsertActivityUseCase
import com.example.onehourapp.dialog.domain.ValidateNameUseCase
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.Category
import com.example.onehourapp.domain.usecase.GetCategoriesUseCase
import com.example.onehourapp.presentation.helpers.toHexString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryDialogViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val validateName: ValidateNameUseCase,
    private val insertCategory: InsertCategoryUseCase,
    private val insertActivity: InsertActivityUseCase
) :ViewModel(){
    private val _categories = getCategoriesUseCase.invoke().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )
    private val _state = MutableStateFlow(AddCategoryDialogState())
    val state = combine(_state, _categories) { state, categories ->
        state.copy(
            categories = categories
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddCategoryDialogState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()
    fun onEvent(event: AddCategoryDialogEvent){
        when(event){
            is AddCategoryDialogEvent.SelectedColorChanged -> {
                _state.update {
                    it.copy(
                        selectedColor = event.color
                    )
                }
            }
            is AddCategoryDialogEvent.NameChanged ->{
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }
            AddCategoryDialogEvent.OnConfirm -> {
                val nameResult = validateName.invoke(_state.value.name)
                val colorSelected = _state.value.selectedColor != null
                if(!nameResult.successful || !colorSelected){
                    _state.update {
                        it.copy(
                            nameError = nameResult.errorMessage,
                            colorError = colorSelected
                        )
                    }
                    return
                }
                viewModelScope.launch {
                    val categoryId = insertCategory(Category(
                        name = state.value.name,
                        color = (state.value.selectedColor?: Color.Unspecified).toHexString()
                    ))
                    insertActivity(
                        Activity(
                            id = 0,
                            name = state.value.name,
                            categoryId = categoryId
                        )
                    )
                    validationEventChannel.send(ValidationEvent.SUCCESS)
                }
            }
        }
    }
}
