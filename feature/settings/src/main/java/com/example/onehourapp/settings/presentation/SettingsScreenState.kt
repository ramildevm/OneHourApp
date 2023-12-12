package com.example.onehourapp.settings.presentation

import com.example.onehourapp.common.GoogleResponseResult

data class SettingsScreenState(
    var notificationOnCheck:Boolean = true,
    var notificationStartHour:Int = 0,
    var notificationEndHour:Int = 0,
    var googleResponseResult: GoogleResponseResult = GoogleResponseResult.DEFAULT
)