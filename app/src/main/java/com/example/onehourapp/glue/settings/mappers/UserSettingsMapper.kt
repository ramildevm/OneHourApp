package com.example.onehourapp.glue.settings.mappers

import com.example.onehourapp.data.database.models.ActivityEntity
import com.example.onehourapp.data.database.models.UserSettingsEntity
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.UserSettings

fun UserSettingsEntity.toModel():UserSettings{
    return UserSettings(
        id = this.id,
        lastAddedActivityId = this.lastAddedActivityId,
        lastAddedDate = this.lastAddedDate,
        notificationStartHour = this.notificationStartHour,
        notificationEndHour = this.notificationEndHour,
    )
}