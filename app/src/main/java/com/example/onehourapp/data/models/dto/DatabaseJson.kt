package com.example.onehourapp.data.models.dto

import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.data.models.Category

data class DatabaseJson(
    val categories:List<Category> = emptyList(),
    val activities:List<Activity> = emptyList(),
    val records:List<ActivityRecord> = emptyList()
)
