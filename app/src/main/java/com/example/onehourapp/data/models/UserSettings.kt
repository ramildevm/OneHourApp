package com.example.onehourapp.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Activity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("lastAddedActivityId"),
        onDelete = ForeignKey.SET_DEFAULT,
)])
data class UserSettings(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val lastAddedActivityId:Int=0,
    val lastAddedDate:Long=0L,
    val sleepActivityStartHour:Int=0,
    val sleepActivityEndHour:Int=0
)
