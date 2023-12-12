package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.onehourapp.data.database.models.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun getCategories(): Flow<List<CategoryEntity>>
    @Query("SELECT * FROM Category WHERE id=:id LIMIT 1")
    fun getCategoryById(id: Int): CategoryEntity
    @Upsert
    suspend fun insertCategory(category: CategoryEntity) : Long
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)


}