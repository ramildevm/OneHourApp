package com.example.onehourapp.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "UserSettings",
    foreignKeys = [ForeignKey(
        entity = ActivityEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("lastAddedActivityId"),
        onDelete = ForeignKey.SET_DEFAULT,
)])
data class UserSettingsEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val lastAddedActivityId:Int=0,
    val lastAddedDate:Long=0L,
    val notificationStartHour:Int=0,
    val notificationEndHour:Int=0
)
