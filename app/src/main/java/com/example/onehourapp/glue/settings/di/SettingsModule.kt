package com.example.onehourapp.glue.settings.di

import com.example.onehourapp.data.database.helpers.GoogleDriveHelper
import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.domain.repositories.UserSettingsRepository
import com.example.onehourapp.glue.activity.AdapterActivityRepository
import com.example.onehourapp.glue.settings.AdapterGoogleDriveRepository
import com.example.onehourapp.glue.settings.AdapterNotificationRepository
import com.example.onehourapp.glue.settings.AdapterUserSettingsRepository
import com.example.onehourapp.settings.domain.repositories.GoogleRepository
import com.example.onehourapp.settings.domain.repositories.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SettingsModule {
    @Binds
    fun bindUserSettingsRepository(settingsRepository: AdapterUserSettingsRepository): UserSettingsRepository
    @Binds
    fun bindNotificationRepository(notificationRepository: AdapterNotificationRepository): NotificationRepository
    @Binds
    fun bindGoogleDriveRepository(googleDriveRepository: AdapterGoogleDriveRepository): GoogleRepository
}