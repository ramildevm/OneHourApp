package com.example.onehourapp.dialog.activity.presentation

import com.example.onehourapp.domain.models.Category

data class AddActivityDialogState(
    var selectedCategory: Category = Category(0,"",""),
    var name:String = "",
    var nameError:String? = null
)
