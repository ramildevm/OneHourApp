package com.example.onehourapp.data.database.di

import com.example.onehourapp.data.ActivityDataRepositoryImpl
import com.example.onehourapp.data.ActivityRecordDataRepositoryImpl
import com.example.onehourapp.data.CategoryDataRepositoryImpl
import com.example.onehourapp.data.GoogleDriveHelperImpl
import com.example.onehourapp.data.UserSettingsDataRepositoryImpl
import com.example.onehourapp.data.database.helpers.GoogleDriveHelper
import com.example.onehourapp.data.database.repositories.ActivityDataRepository
import com.example.onehourapp.data.database.repositories.ActivityRecordDataRepository
import com.example.onehourapp.data.database.repositories.CategoryDataRepository
import com.example.onehourapp.data.database.repositories.UserSettingsDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindActivityRecordDataRepository(
        activityRecordDataRepository: ActivityRecordDataRepositoryImpl
    ): ActivityRecordDataRepository
    @Binds
    @Singleton
    fun bindActivityDataRepository(
        activityDataRepository: ActivityDataRepositoryImpl
    ): ActivityDataRepository
    @Binds
    @Singleton
    fun bindCategoryDataRepository(
        categoryDataRepository: CategoryDataRepositoryImpl
    ): CategoryDataRepository
    @Binds
    @Singleton
    fun bindUserSettingsDataRepository(
        userSettingsDataRepository: UserSettingsDataRepositoryImpl
    ): UserSettingsDataRepository
    @Binds
    @Singleton
    fun bindGoogleDriveRepository(
        googleDriveHelper: GoogleDriveHelperImpl
    ): GoogleDriveHelper
}