
package com.example.onehourapp.data

import com.example.onehourapp.data.database.models.CategoryEntity
import com.example.onehourapp.data.database.repositories.CategoryDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class CategoryDataRepositoryImpl @Inject constructor(
    @Named("category") private val categoryDao: com.example.onehourapp.data.database.dao.CategoryDao,
    @Named("activity") private val activityDao: com.example.onehourapp.data.database.dao.ActivityDao
): CategoryDataRepository {
    override fun getCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getCategories()
    }

    override fun getCategoryById(categoryId: Int): CategoryEntity {
        return categoryDao.getCategoryById(categoryId)
    }
    override suspend fun insertCategory(category: CategoryEntity) = categoryDao.insertCategory(category)

    override suspend fun deleteCategory(categoryId: Int): Boolean {
        val category = categoryDao.getCategoryById(categoryId)
        val activitiesByCategory = activityDao.getActivitiesByCategoryId(category.id).first()
        if (activitiesByCategory.size == 1){
            activityDao.deleteActivity(activitiesByCategory.first())
            categoryDao.deleteCategory(category)
        }
        else{
            return false
        }
        return true
    }

}

