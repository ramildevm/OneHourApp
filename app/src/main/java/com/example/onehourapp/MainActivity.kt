package com.example.onehourapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.onehourapp.data.preferences.SharedPreferencesKeys
import com.example.onehourapp.helpers.NotificationsAlarmManager
import com.example.onehourapp.ui.graphs.RootNavigationGraph
import com.example.onehourapp.ui.theme.OneHourAppTheme
import com.example.onehourapp.ui.viewmodels.UserSettingsViewModel
import com.example.onehourapp.utils.CalendarUtil
import com.example.onehourapp.utils.SharedPreferencesUtil
import com.example.onehourapp.utils.SystemUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsVM:UserSettingsViewModel by viewModels()
    val num:Int = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alarmManager = NotificationsAlarmManager(applicationContext)
        alarmManager.cancelScheduleNotifications()
        alarmManager.startScheduleNotifications()
        val currentLocale = Locale.getDefault()
        val languageCode = currentLocale.language
        val localeLanguage = SharedPreferencesUtil.getSharedStringData(this, SharedPreferencesKeys.PREF_LOCALE_LANGUAGE)
        if(languageCode!=localeLanguage)
            SystemUtil.setLocale(this, localeLanguage)

        settingsVM.updateUserSettingsAddingData(lastAddedDate = CalendarUtil.getHourCheckedCurrentDayMillis())
        setContent {
            OneHourAppTheme(darkTheme = true) {
                RootNavigationGraph(navController = rememberNavController())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OneHourAppTheme {
        RootNavigationGraph(navController = rememberNavController())
    }
}

