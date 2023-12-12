package com.example.onehourapp.settings.presentation

sealed class SettingsScreenEvent {
    data class StatusChanged(val isEnabled: Boolean) : SettingsScreenEvent()
    data class StartHourChanged(val value: Int) : SettingsScreenEvent()
    data class EndHourChanged(val value: Int) : SettingsScreenEvent()
    object SyncWithGoogle : SettingsScreenEvent()
}
