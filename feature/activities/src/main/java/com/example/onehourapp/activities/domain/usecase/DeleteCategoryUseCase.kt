package com.example.onehourapp.activities.domain.usecase

import com.example.onehourapp.domain.repositories.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: Int) = repository.deleteCategoryById(categoryId)
}