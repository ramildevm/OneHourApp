package com.example.onehourapp.settings.domain.repositories

interface NotificationRepository {
    fun getNotificationStatus():Boolean
    fun setNotificationStatus(enabled:Boolean)
    fun getNotificationStartHour(): Int
    suspend fun setNotificationStartHour(startHour: Int)
    fun getNotificationEndHour(): Int
    suspend fun setNotificationEndHour(endHour: Int)
}