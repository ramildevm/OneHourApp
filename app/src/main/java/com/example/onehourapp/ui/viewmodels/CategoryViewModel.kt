package com.example.onehourapp.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehourapp.data.models.Category
import com.example.onehourapp.data.repositories.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
): ViewModel() {
    internal val allCategories: Flow<List<Category>> = repository.getCategories()
    val insertResult = MutableLiveData<Int>()
    fun insertCategory(category: Category){
        viewModelScope.launch{
            val id = repository.insertCategory(category)
            insertResult.postValue(id.toInt())
        }
    }
    fun deleteCategory(category: Category){
        viewModelScope.launch{
            repository.deleteCategory(category)
        }
    }

}