package com.example.onehourapp.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Activity",
    foreignKeys = [ForeignKey(
    entity = CategoryEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("categoryId"),
    onDelete = ForeignKey.CASCADE,
)])
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name:String,
    val categoryId:Int
)
