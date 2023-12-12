package com.example.onehourapp.settings.domain.repositories

import com.example.onehourapp.common.GoogleResponseResult

interface GoogleRepository {
    suspend fun syncWithGoogle():GoogleResponseResult
}
