package com.example.onehourapp.dialog.category.presentation

import androidx.compose.ui.graphics.Color
import com.example.onehourapp.domain.models.Category

data class AddCategoryDialogState(
    val categories:List<Category> = emptyList(),
    var selectedColor: Color? = null,
    var colorError: Boolean = false,
    var name:String = "",
    var nameError:String? = null
)
