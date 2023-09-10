package com.example.onehourapp.ui.screens.home.settings

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.onehourapp.helpers.AddNotificationHelper
import com.example.onehourapp.services.NotificationService
import com.example.onehourapp.ui.components.NumberPicker
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.BackgroundSecondColor
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.ui.theme.MainFont
import com.example.onehourapp.utils.CalendarUtil
import com.example.onehourapp.utils.SharedPreferencesUtil


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsContent(){
    Scaffold(modifier = Modifier.background(BackgroundColor)) {
        val context = LocalContext.current
        Column(
            Modifier
                .padding(it)
                .background(BackgroundColor)
                .fillMaxSize()) {
            val start = SharedPreferencesUtil.getSharedIntData(context,"start_hour",8)
            val end = SharedPreferencesUtil.getSharedIntData(context,"end_hour",0)

            val flag = SharedPreferencesUtil.getSharedBooleanData(context, "isNotificationsRunning")
            var checked by remember {
                mutableStateOf(flag)
            }
            Box(modifier = Modifier.fillMaxWidth()){
                Text(modifier = Modifier.align(Alignment.CenterStart), text = "Turn of notifications", style = MainFont, textAlign = TextAlign.Center)
                Switch(modifier = Modifier.align(Alignment.CenterEnd), checked = checked, onCheckedChange = {
                    if (checked) {
                        AddNotificationHelper.cancelAlarmRTC(context)
                        AddNotificationHelper.disableBootReceiver(context)
                    }
                    else{
                        AddNotificationHelper.scheduleRepeatingRTCNotification(context,start ,end )
                        AddNotificationHelper.enableBootReceiver(context)
                    }
                    checked =!checked
                })
            }
            Box(modifier = Modifier.fillMaxWidth()){
                Text(modifier = Modifier.align(Alignment.CenterStart), text = "Start", style = MainFont, textAlign = TextAlign.Center)
                var pickerValue by remember { mutableStateOf(start) }

                NumberPicker(
                    modifier = Modifier.wrapContentWidth().align(Alignment.CenterEnd),
                    value = pickerValue,
                    range = 0..23,
                    dividersColor = MainColorSecondRed,
                    textStyle = MainFont,
                    onValueChange = {
                        pickerValue = it
                    }
                )
            }
            Box(modifier = Modifier.fillMaxWidth()){
                Text(modifier = Modifier.align(Alignment.CenterStart), text = "Start", style = MainFont, textAlign = TextAlign.Center)
                var pickerValue by remember { mutableStateOf(end) }
                NumberPicker(
                    modifier = Modifier.wrapContentWidth().align(Alignment.CenterEnd),
                    value = pickerValue,
                    range = 0..23,
                    dividersColor = MainColorSecondRed,
                    textStyle = MainFont,
                    onValueChange = {
                        pickerValue = it
                    }
                )
            }


        }
    }
}