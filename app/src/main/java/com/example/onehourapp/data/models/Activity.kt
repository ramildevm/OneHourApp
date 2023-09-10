package com.example.onehourapp.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
    entity = Category::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("categoryId"),
    onDelete = ForeignKey.CASCADE,
)])
data class Activity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name:String,
    val categoryId:Int
)
