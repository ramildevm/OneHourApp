package com.example.onehourapp.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ActivityRecord",
    foreignKeys = [ForeignKey(
        entity = ActivityEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("activityId"),
        onDelete = ForeignKey.CASCADE,
    )])
data class ActivityRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val activityId:Int,
    val timestamp:Long =0L
)
