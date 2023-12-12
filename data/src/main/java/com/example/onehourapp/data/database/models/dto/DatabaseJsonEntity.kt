package com.example.onehourapp.data.database.models.dto

import com.example.onehourapp.data.database.models.ActivityEntity
import com.example.onehourapp.data.database.models.ActivityRecordEntity
import com.example.onehourapp.data.database.models.CategoryEntity

data class DatabaseJsonEntity(
    val categories:List<CategoryEntity> = emptyList(),
    val activities:List<ActivityEntity> = emptyList(),
    val records:List<ActivityRecordEntity> = emptyList()
)
