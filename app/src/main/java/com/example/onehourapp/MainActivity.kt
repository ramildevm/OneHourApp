package com.example.onehourapp

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.onehourapp.ui.graphs.RootNavigationGraph
import com.example.onehourapp.helpers.AddNotificationHelper
import com.example.onehourapp.services.NotificationService
import com.example.onehourapp.ui.theme.OneHourAppTheme
import com.example.onehourapp.utils.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mContext = getApplicationContext()
        val isNotificationsRunning = SharedPreferencesUtil.getSharedBooleanData(this,"isNotificationsRunning")
        if(!isNotificationsRunning){AddNotificationHelper.createNotificationChannel(mContext)
            AddNotificationHelper.scheduleRepeatingRTCNotification(mContext,8 ,0 )
            AddNotificationHelper.enableBootReceiver(mContext)

            SharedPreferencesUtil.setSharedData(mContext,"start_hour",8)
            SharedPreferencesUtil.setSharedData(mContext,"end_hour",0)

        }
        setContent {
            OneHourAppTheme {
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