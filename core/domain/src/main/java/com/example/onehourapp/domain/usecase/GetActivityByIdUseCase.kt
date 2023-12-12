package com.example.onehourapp.domain.usecase
import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.domain.repositories.CategoryRepository
import javax.inject.Inject
class GetActivityByIdUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    operator fun invoke(activityId:Int) = repository.getActivityById(activityId)
}