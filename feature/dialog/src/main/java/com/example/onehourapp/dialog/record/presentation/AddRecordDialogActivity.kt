package com.example.onehourapp.dialog.record.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.onehourapp.NOTIFICATION_CURRENT_TIME
import com.example.onehourapp.common.preferences.SharedPreferencesKeys
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AddRecordDialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extraData = intent.extras?.getLong(NOTIFICATION_CURRENT_TIME, 0L)

        val currentTime = extraData ?: 0L
        var day = com.example.onehourapp.common.utils.CalendarUtil.getCurrentDayMillis()
        var hour = com.example.onehourapp.common.utils.CalendarUtil.getCurrentHour()
        if(currentTime != 0L){
            day = com.example.onehourapp.common.utils.CalendarUtil.getCurrentDayMillis(currentTime)
            hour = com.example.onehourapp.common.utils.CalendarUtil.getCurrentHour(currentTime)
        }
        val currentLocale = Locale.getDefault()
        val languageCode = currentLocale.language
        val localeLanguage = com.example.onehourapp.common.utils.SharedPreferencesUtil.getSharedStringData(this, SharedPreferencesKeys.PREF_LOCALE_LANGUAGE)
        if(languageCode!=localeLanguage)
            com.example.onehourapp.common.utils.SystemUtil.setLocale(this, localeLanguage)
        setContent {
            com.example.onehourapp.theme.ui.OneHourAppTheme(darkTheme = true) {
                AddRecordDialog(
                    date = day,
                    hour = hour,
                    AddCallerType.NOTIFICATION,
                    onDismiss = { finish() },
                    notifyChange = { finish() }
                )
            }
        }
    }
}