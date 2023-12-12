package com.example.onehourapp.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(
    tableName = "Category"
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name: String,
    val color: String
)
