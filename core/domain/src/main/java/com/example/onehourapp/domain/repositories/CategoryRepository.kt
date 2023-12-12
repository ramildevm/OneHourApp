package com.example.onehourapp.domain.repositories

import com.example.onehourapp.domain.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
    suspend fun deleteCategoryById(categoryId: Int) :Boolean
    suspend fun insertCategory(category: Category) : Int
}