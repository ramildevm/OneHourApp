package com.example.onehourapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.onehourapp.helpers.createNotificationChannel
import com.example.onehourapp.helpers.scheduleNotification
import com.example.onehourapp.ui.graphs.RootNavigationGraph
import com.example.onehourapp.ui.theme.OneHourAppTheme
import com.example.onehourapp.ui.viewmodels.UserSettingsViewModel
import com.example.onehourapp.utils.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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