package com.example.onehourapp.domain.usecase
import com.example.onehourapp.domain.repositories.CategoryRepository
import javax.inject.Inject
class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke() = repository.getCategories()
}