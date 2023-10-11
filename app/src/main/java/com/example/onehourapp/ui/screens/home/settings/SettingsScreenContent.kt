package com.example.onehourapp.ui.screens.home.settings

import android.app.NotificationManager
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onehourapp.R
import com.example.onehourapp.data.preferences.SharedPreferencesKeys
import com.example.onehourapp.helpers.AddNotificationHelper
import com.example.onehourapp.helpers.NotificationsAlarmManager
import com.example.onehourapp.helpers.createNotificationChannel
import com.example.onehourapp.helpers.deleteNotificationChannel
import com.example.onehourapp.helpers.isNotificationChannelEnabled
import com.example.onehourapp.services.NotificationService
import com.example.onehourapp.ui.components.NumberPicker
import com.example.onehourapp.ui.helpers.gesturesDisabled
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.BackgroundSecondColor
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.ui.theme.MainFont
import com.example.onehourapp.ui.theme.MainFontMedium
import com.example.onehourapp.ui.theme.MainFontSmall
import com.example.onehourapp.ui.viewmodels.UserSettingsViewModel
import com.example.onehourapp.utils.CalendarUtil
import com.example.onehourapp.utils.SharedPreferencesUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsContent(){
    Scaffold(modifier = Modifier.background(BackgroundColor)) {
        val userSettingsViewModel:UserSettingsViewModel = hiltViewModel()
        val context = LocalContext.current
        Column(
            Modifier
                .padding(it)
                .background(BackgroundColor)
                .padding(horizontal = 16.dp)
                .fillMaxSize()) {

            val flag = isNotificationChannelEnabled(context)
            var checked by remember {
                mutableStateOf(flag)
            }
            val start = SharedPreferencesUtil.getSharedIntData(context.applicationContext, SharedPreferencesKeys.PREF_START_HOUR)
            val end = SharedPreferencesUtil.getSharedIntData(context.applicationContext, SharedPreferencesKeys.PREF_END_HOUR)
            var pickerStartValue by remember { mutableIntStateOf(start) }
            var pickerEndValue by remember { mutableIntStateOf(end) }
            Text(
                text = stringResource(id = R.string.settings),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(
                        BackgroundColor
                    ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                color = Color.Red
            )
            Box(modifier = Modifier.fillMaxWidth()){
                Text(modifier = Modifier.align(Alignment.CenterStart), text = stringResource(R.string.enable_notifications), style = MainFont, textAlign = TextAlign.Center)
                Switch(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = checked,
                    colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray),
                    onCheckedChange = { flag ->
                        if (flag)
                            createNotificationChannel(context)
                        else
                            deleteNotificationChannel(context)
                        checked = flag
                        userSettingsViewModel.updateUserSettingsNotificationStatus(flag)
                    }
                )
            }
            val boxModifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val effect = RenderEffect.createColorFilterEffect(
                            ColorMatrixColorFilter(
                                ColorMatrix(
                                    floatArrayOf(
                                        0.3f, 0.3f, 0.3f, 0f, 0f,
                                        0.3f, 0.3f, 0.3f, 0f, 0f,
                                        0.3f, 0.3f, 0.3f, 0f, 0f,
                                        0f, 0f, 0f, 1f, 0f
                                    )
                                )
                            )
                        )
                        if (!checked)
                            renderEffect = effect.asComposeRenderEffect()
                    }
                }
                .alpha(if (checked) 1f else 0.65f)
            val alarmManager = NotificationsAlarmManager(context)
            Box(modifier = boxModifier){
                Text(modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp), text = stringResource(R.string.start_hour), style = MainFontMedium, textAlign = TextAlign.Center)
                NumberPicker(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterEnd),
                    value = pickerStartValue,
                    range = 0..23,
                    dividersColor = MainColorSecondRed,
                    textStyle = MainFont,
                    enabled = checked,
                    onValueChange = { value ->
                        pickerStartValue = value
                        SharedPreferencesUtil.setSharedData(context, SharedPreferencesKeys.PREF_START_HOUR,value)
                        alarmManager.cancelScheduleNotifications()
                        alarmManager.startScheduleNotifications()
                    }
                )
            }
            Box(modifier = boxModifier){
                Text(modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp), text = stringResource(R.string.end_hour), style = MainFontMedium, textAlign = TextAlign.Center)
                NumberPicker(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterEnd),
                    value = pickerEndValue,
                    range = 1..24,
                    dividersColor = MainColorSecondRed,
                    textStyle = MainFont,
                    enabled = checked,
                    onValueChange = { value ->
                        pickerEndValue = value
                        SharedPreferencesUtil.setSharedData(context,SharedPreferencesKeys.PREF_END_HOUR,value)
                        alarmManager.cancelScheduleNotifications()
                        alarmManager.startScheduleNotifications()
                    }
                )
            }
            Log.e("Erre","$start $end")
        }
    }
}