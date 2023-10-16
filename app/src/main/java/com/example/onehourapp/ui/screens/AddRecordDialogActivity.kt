package com.example.onehourapp.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.onehourapp.data.preferences.SharedPreferencesKeys
import com.example.onehourapp.receivers.currentTime
import com.example.onehourapp.ui.screens.home.AddCallerType
import com.example.onehourapp.ui.screens.home.AddRecordDialog
import com.example.onehourapp.ui.theme.OneHourAppTheme
import com.example.onehourapp.utils.CalendarUtil
import com.example.onehourapp.utils.SharedPreferencesUtil
import com.example.onehourapp.utils.SystemUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddRecordDialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extraData = intent.extras?.getLong(currentTime, 0L)

        val currentTime = extraData ?: 0L
        var day = CalendarUtil.getCurrentDayMillis()
        var hour = CalendarUtil.getCurrentHour()
        if(currentTime != 0L){
            day = CalendarUtil.getCurrentDayMillis(currentTime)
            hour = CalendarUtil.getCurrentHour(currentTime)
        }
        val currentLocale = Locale.getDefault()
        val languageCode = currentLocale.language
        val localeLanguage = SharedPreferencesUtil.getSharedStringData(this, SharedPreferencesKeys.PREF_LOCALE_LANGUAGE)
        if(languageCode!=localeLanguage)
            SystemUtil.setLocale(this, localeLanguage)
        setContent {
            OneHourAppTheme(darkTheme = true) {
                AddRecordDialog (
                    date = day,
                    hour = hour,
                    AddCallerType.NOTIFICATION,
                    onDismiss = {finish()},
                    notifyChange = {finish()}
                )
            }
        }
    }

}