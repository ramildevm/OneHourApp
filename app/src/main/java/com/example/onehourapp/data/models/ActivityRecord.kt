package com.example.onehourapp.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Activity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("activityId"),
        onDelete = ForeignKey.CASCADE,
    )])
data class ActivityRecord(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val activityId:Int,
    val timestamp:Long =0L
)
