package com.example.onehourapp.activities.presentation

sealed class ActivitiesScreenEvent {
    data class SortTypeChanged(val sortType: CategorySortType) : ActivitiesScreenEvent()
    data class DeleteCategory(val categoryId: Int) : ActivitiesScreenEvent()
    data class DeleteActivity(val activityId: Int) : ActivitiesScreenEvent()

}
