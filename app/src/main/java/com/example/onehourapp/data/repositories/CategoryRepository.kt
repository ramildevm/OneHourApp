package com.example.onehourapp.data.repositories

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import com.example.onehourapp.data.database.dao.ActivityDao
import com.example.onehourapp.data.database.dao.CategoryDao
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Named

class CategoryRepository @Inject constructor(
    @Named("category") private val categoryDao: CategoryDao,
    @Named("activity") private val activityDao: ActivityDao
){
    fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories()
    }
    suspend fun insertOrUpdateCategory(category: Category){
        categoryDao.insertUpdateCategory(category)
    }
    suspend fun deleteCategory(category: Category): Boolean{
        val activitiesByCategory = activityDao.getActivitiesByCategoryId(category.id).last()
        if(activitiesByCategory.isEmpty())
            categoryDao.deleteCategory(category)
        else{
            return false
        }
        return true
    }
}