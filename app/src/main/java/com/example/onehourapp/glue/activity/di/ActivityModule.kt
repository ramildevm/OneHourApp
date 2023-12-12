package com.example.onehourapp.glue.activity.di

import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.glue.activity.AdapterActivityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ActivityModule {
    @Binds
    fun bindActivityRepository(activityRepository: AdapterActivityRepository): ActivityRepository
}