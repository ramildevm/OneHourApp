package com.example.onehourapp.data.database.helpers

import com.example.onehourapp.common.GoogleResponseResult

interface GoogleDriveHelper {
    suspend fun syncWithDrive() :GoogleResponseResult
}