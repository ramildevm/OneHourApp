package com.example.onehourapp.data.repositories

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import com.example.onehourapp.data.database.dao.ActivityDao
import com.example.onehourapp.data.database.dao.CategoryDao
import com.example.onehourapp.data.database.dao.UserSettingsDao
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.Category
import com.example.onehourapp.data.models.UserSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Named

class UserSettingsRepository @Inject constructor(
    @Named("userSettings") private val userSettingsDao: UserSettingsDao
){
    fun getUserSettings() = userSettingsDao.getUserSettings()
    suspend fun insertOrUpdateUserSettings(userSettings: UserSettings) = userSettingsDao.insertUpdateUserSettings(userSettings)
}