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
import com.example.onehourapp.ui.screens.home.ComposeAlertDialogExample
import com.example.onehourapp.ui.theme.OneHourAppTheme
import com.example.onehourapp.utils.CalendarUtil


class AddRecordDialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hour = intent.extras?.getInt("hour",CalendarUtil.getCurrentHour())
        val day = intent.extras?.getInt("day",CalendarUtil.getCurrentDay())
        setContent {
            OneHourAppTheme {
                var showDialog by remember { mutableStateOf(true) }
                if (showDialog)
                ComposeAlertDialogExample (
                    day = day!!,
                   hour = hour!!,
                   onDismiss = {showDialog = false}
                )
                else
                    finish()
            }
        }
    }

}