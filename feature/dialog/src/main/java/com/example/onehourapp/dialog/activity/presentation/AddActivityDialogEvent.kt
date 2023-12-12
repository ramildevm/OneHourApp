package com.example.onehourapp.dialog.activity.presentation

import com.example.onehourapp.domain.models.Category

sealed class AddActivityDialogEvent {
    data class SelectedCategoryChanged(val category: Category): AddActivityDialogEvent()
    data class NameChanged(val name:String): AddActivityDialogEvent()
    object OnConfirm: AddActivityDialogEvent()
}