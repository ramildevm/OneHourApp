package com.example.onehourapp.data.database.models.dto

import androidx.room.ColumnInfo

data class CategoryCountEntity(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "count") val count: Int
)
