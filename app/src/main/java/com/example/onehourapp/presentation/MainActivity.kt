package com.example.onehourapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.onehourapp.common.preferences.SharedPreferencesKeys
import com.example.onehourapp.notification.NotificationsAlarmManager
import com.example.onehourapp.presentation.navigation.RootNavigationGraph
import com.example.onehourapp.presentation.viewmodels.UserSettingsViewModel
import com.example.onehourapp.theme.ui.OneHourAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alarmManager = NotificationsAlarmManager(applicationContext)
        alarmManager.cancelScheduleNotifications()
        alarmManager.startScheduleNotifications()
        val currentLocale = Locale.getDefault()
        val languageCode = currentLocale.language
        val localeLanguage = com.example.onehourapp.common.utils.SharedPreferencesUtil.getSharedStringData(this, SharedPreferencesKeys.PREF_LOCALE_LANGUAGE)
        if(languageCode!=localeLanguage)
            com.example.onehourapp.common.utils.SystemUtil.setLocale(this, localeLanguage)

        setContent {
            OneHourAppTheme(darkTheme = true) {
                val viewModel = hiltViewModel<MainActivityViewModel>()
                val date = com.example.onehourapp.common.utils.CalendarUtil.getHourCheckedCurrentDayMillis()
                viewModel.updateUserSettingsAddingDate(date)
                RootNavigationGraph(navController = rememberNavController())
//                Box(modifier = Modifier.fillMaxSize().background(Color.Black)){
//                    var exposed by remember {mutableStateOf(false)}
//                    var text by remember {mutableStateOf("")}
//                    ExposedDropdownMenuBox(modifier = Modifier.fillMaxWidth().height(50.dp), expanded = exposed, onExpandedChange = {exposed = !exposed} ) {
//                        TextField(value = text, onValueChange = {text = it})
//                        ExposedDropdownMenu(expanded = exposed, onDismissRequest = { exposed=false }) {
//                            for(i in 0..3){
//
//                                DropdownMenuItem(onClick = { text = i.toString()}) {
//                                    Text(i.toString())
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    com.example.onehourapp.theme.ui.OneHourAppTheme {
        RootNavigationGraph(navController = rememberNavController())
    }
}

