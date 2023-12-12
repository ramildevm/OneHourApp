package com.example.onehourapp.dialog.category.presentation

import androidx.compose.ui.graphics.Color
import com.example.onehourapp.domain.models.Category

sealed class AddCategoryDialogEvent {
    data class NameChanged(val name:String): AddCategoryDialogEvent()
    data class SelectedColorChanged(val color: Color?): AddCategoryDialogEvent()
    object OnConfirm: AddCategoryDialogEvent()
}