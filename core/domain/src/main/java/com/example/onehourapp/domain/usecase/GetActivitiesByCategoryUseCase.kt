package com.example.onehourapp.domain.usecase
import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.domain.repositories.CategoryRepository
import javax.inject.Inject
class GetActivitiesByCategoryUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    operator fun invoke(categoryId:Int) = repository.getActivitiesByCategoryId(categoryId)
}