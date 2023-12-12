package com.example.onehourapp.activities.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.activities.domain.usecase.DeleteActivityUseCase
import com.example.onehourapp.activities.domain.usecase.DeleteCategoryUseCase
import com.example.onehourapp.domain.models.Category
import com.example.onehourapp.domain.usecase.GetActivitiesByCategoryUseCase
import com.example.onehourapp.domain.usecase.GetCategoriesUseCase
import com.example.onehourapp.presentation.components.SortingHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivitiesScreenViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase,
    private val getActivitiesByCategory: GetActivitiesByCategoryUseCase,
    private val deleteCategory: DeleteCategoryUseCase,
    private val deleteActivity: DeleteActivityUseCase,
) : ViewModel(){
    private val _sortType = MutableStateFlow(CategorySortType.DEFAULT)
    private val _categories = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                CategorySortType.DEFAULT -> getCategories.invoke()
                CategorySortType.COLOR -> getCategories.invoke().map { list-> list.sortedWith{ item1, item2->
                    SortingHelper.compareColors(item1.color, item2.color)
                } }
                CategorySortType.NAME -> getCategories.invoke().map { list-> list.sortedBy { it.name } }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ActivitiesScreenState())
    val state = combine(_state, _sortType, _categories) { state, sortType, categories ->
        state.copy(
            categories = categories,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActivitiesScreenState())
    fun onEvent(event:ActivitiesScreenEvent){
        when(event){
            is ActivitiesScreenEvent.SortTypeChanged -> {
                _sortType.value = event.sortType
            }
            is ActivitiesScreenEvent.DeleteActivity -> {
                viewModelScope.launch {
                    deleteActivity(event.activityId)
                }
            }
            is ActivitiesScreenEvent.DeleteCategory -> {
                viewModelScope.launch {
                    deleteCategory(event.categoryId)
                }
            }
        }
    }
    fun getActivitiesByCategoryId(categoryId:Int) = getActivitiesByCategory.invoke(categoryId)
}
