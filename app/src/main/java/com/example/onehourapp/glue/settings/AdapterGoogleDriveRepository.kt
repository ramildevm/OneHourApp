package com.example.onehourapp.glue.settings

import android.content.Context
import com.example.onehourapp.common.GoogleResponseResult
import com.example.onehourapp.data.database.helpers.GoogleDriveHelper
import com.example.onehourapp.data.database.repositories.UserSettingsDataRepository
import com.example.onehourapp.notification.NotificationChannelBuilder
import com.example.onehourapp.settings.domain.repositories.GoogleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AdapterGoogleDriveRepository @Inject constructor(
    private val googleDriveHelper: GoogleDriveHelper
) : GoogleRepository {
    override suspend fun syncWithGoogle(): GoogleResponseResult {
        return googleDriveHelper.syncWithDrive()
    }

}