package com.example.onehourapp.data.database.di

import android.app.Application
import androidx.room.Room
import com.example.onehourapp.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        app: Application,
        callback: AppDatabase.Callback
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "OneHourDB.db"
    )
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    @Named("category")
    fun provideCategoryDao(db: AppDatabase) = db.categoryDao
    @Singleton
    @Provides
    @Named("activity")
    fun provideActivityDao(db: AppDatabase) = db.activityDao
    @Singleton
    @Provides
    @Named("activityRecord")
    fun provideActivityRecordDao(db: AppDatabase) = db.activityRecordDao
    @Singleton
    @Provides
    @Named("userSettings")
    fun provideUserSettingsDao(db: AppDatabase) = db.userSettingsDao

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope