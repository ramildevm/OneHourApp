package com.example.onehourapp.dialog.category.domain

import com.example.onehourapp.domain.models.Category
import com.example.onehourapp.domain.repositories.CategoryRepository
import javax.inject.Inject

class InsertCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category) = repository.insertCategory(category)
}