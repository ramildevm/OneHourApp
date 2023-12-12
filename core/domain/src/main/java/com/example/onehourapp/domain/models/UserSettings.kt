package com.example.onehourapp.domain.models

data class UserSettings(
    val id:Int=0,
    val lastAddedActivityId:Int=0,
    val lastAddedDate:Long=0L,
    val notificationStartHour:Int=0,
    val notificationEndHour:Int=0
)
