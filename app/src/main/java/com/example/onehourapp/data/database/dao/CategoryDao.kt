package com.example.onehourapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.onehourapp.data.models.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun getCategories(): Flow<List<Category>>
    @Query("SELECT * FROM Category WHERE id=:id LIMIT 1")
    fun getCategoryById(id: Int): Category
    @Insert
    suspend fun insertCategory(category: Category) : Long
    @Delete
    suspend fun deleteCategory(category: Category)


}