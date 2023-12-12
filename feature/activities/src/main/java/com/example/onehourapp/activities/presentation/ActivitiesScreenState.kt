package com.example.onehourapp.activities.presentation

import com.example.onehourapp.domain.models.Category

data class ActivitiesScreenState(
    val categories: List<Category> = emptyList(),
    val sortType: CategorySortType = CategorySortType.DEFAULT
)