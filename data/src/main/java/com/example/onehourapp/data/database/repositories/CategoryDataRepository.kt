package com.example.onehourapp.data.database.repositories

import com.example.onehourapp.data.database.models.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryDataRepository {

    fun getCategories(): Flow<List<CategoryEntity>>

    fun getCategoryById(categoryId: Int): CategoryEntity

    suspend fun insertCategory(category: CategoryEntity) : Long

    suspend fun deleteCategory(categoryId: Int): Boolean
}
