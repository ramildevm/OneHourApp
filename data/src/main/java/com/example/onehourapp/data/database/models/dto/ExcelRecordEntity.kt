package com.example.onehourapp.data.database.models.dto

import androidx.room.ColumnInfo

data class ExcelRecordEntity(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "activity") val activity: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)
