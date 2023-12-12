package com.example.onehourapp.presentation.home

import androidx.lifecycle.ViewModel
import com.example.onehourapp.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase
):ViewModel() {
    val categories = getCategories()
}