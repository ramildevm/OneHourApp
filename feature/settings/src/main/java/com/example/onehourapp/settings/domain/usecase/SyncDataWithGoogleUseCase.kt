package com.example.onehourapp.settings.domain.usecase

import com.example.onehourapp.common.GoogleResponseResult
import com.example.onehourapp.settings.domain.repositories.GoogleRepository
import javax.inject.Inject

class SyncDataWithGoogleUseCase @Inject constructor(private val repository: GoogleRepository) {
    suspend operator fun invoke():GoogleResponseResult{
        return repository.syncWithGoogle()
    }
}