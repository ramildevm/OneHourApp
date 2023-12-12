package com.example.onehourapp.dialog.domain

import android.content.Context
import com.example.onehourapp.theme.R
import com.example.onehourapp.common.ValidationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ValidateNameUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(name:String):ValidationResult{
        if(name.trim().isEmpty())
            return ValidationResult(
                successful = false,
                errorMessage = context.resources.getString(R.string.empty_field)
            )
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}