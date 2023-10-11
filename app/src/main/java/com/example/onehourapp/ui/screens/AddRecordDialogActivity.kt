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
import com.example.onehourapp.receivers.currentTime
import com.example.onehourapp.ui.screens.home.AddCallerType
import com.example.onehourapp.ui.screens.home.AddRecordDialog
import com.example.onehourapp.ui.theme.OneHourAppTheme
import com.example.onehourapp.utils.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddRecordDialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentTime = intent.extras?.getLong(currentTime,0L)
        val day = if(currentTime != null) CalendarUtil.getCurrentDayMillis(currentTime) else CalendarUtil.getCurrentDayMillis()
        val hour = CalendarUtil.getCurrentHour(currentTime?:0L)
        setContent {
            OneHourAppTheme(darkTheme = true) {
                var showDialog by remember { mutableStateOf(true) }
                if (showDialog)
                AddRecordDialog (
                    date = day,
                    hour = hour,
                    AddCallerType.NOTIFICATION,
                    onDismiss = {showDialog = false},
                    notifyChange = {}
                )
                else
                    finish()
            }
        }
    }

}