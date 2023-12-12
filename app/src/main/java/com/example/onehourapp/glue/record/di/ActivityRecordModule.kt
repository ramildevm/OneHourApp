package com.example.onehourapp.glue.record.di

import com.example.onehourapp.domain.repositories.ActivityRecordRepository
import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.glue.activity.AdapterActivityRepository
import com.example.onehourapp.glue.record.AdapterActivityRecordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ActivityRecordModule {
    @Binds
    fun bindActivityRecordRepository(activityRecordRepository: AdapterActivityRecordRepository): ActivityRecordRepository
}