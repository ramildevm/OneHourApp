package com.example.onehourapp.ui.screens.home.settings

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.os.Build
import android.os.Environment
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TableView
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onehourapp.R
import com.example.onehourapp.data.models.dto.ExcelRecord
import com.example.onehourapp.data.preferences.SharedPreferencesKeys
import com.example.onehourapp.helpers.NotificationChannelBuilder
import com.example.onehourapp.helpers.NotificationsAlarmManager
import com.example.onehourapp.ui.components.NumberPicker
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.ui.theme.MainFont
import com.example.onehourapp.ui.theme.MainFontMedium
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.UserSettingsViewModel
import com.example.onehourapp.utils.ExcelFileMaker
import com.example.onehourapp.utils.SharedPreferencesUtil
import com.example.onehourapp.utils.SystemUtil
import com.example.onehourapp.utils.SystemUtil.getActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsContent(){
    Scaffold(modifier = Modifier.background(BackgroundColor)) {
        val userSettingsViewModel:UserSettingsViewModel = hiltViewModel()
        val activityRecordViewModel:ActivityRecordViewModel = hiltViewModel()
        val context = LocalContext.current
        Column(
            Modifier
                .padding(it)
                .background(BackgroundColor)
                .padding(horizontal = 16.dp)
                .fillMaxSize()) {

            val flag = NotificationChannelBuilder.isNotificationChannelEnabled(context)
            var checked by remember {
                mutableStateOf(flag)
            }

            val userSettings = userSettingsViewModel.getUserSettings()
            val start = userSettings.notificationStartHour
            val end = userSettings.notificationEndHour
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    val currentLocale = Locale.getDefault()
                    val languageCode = currentLocale.language
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = stringResource(R.string.language),
                        style = MainFont,
                        textAlign = TextAlign.Center
                    )
                    Row(Modifier.align(Alignment.CenterEnd), verticalAlignment = Alignment.CenterVertically){
                        TextButton(onClick = {
                            if(languageCode=="en") {
                                SystemUtil.setLocale(context.getActivity(), "ru")
                                SharedPreferencesUtil.setSharedData(context.getActivity(), SharedPreferencesKeys.PREF_LOCALE_LANGUAGE,"ru")
                            }
                        }) {
                            Text(text = "Ru", fontSize = 20.sp, color = if(languageCode=="ru") MainColorSecondRed else Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier
                            .height(30.dp)
                            .width(2.dp)
                            .background(MainColorSecondRed)
                            .clip(
                                RoundedCornerShape(5.dp)
                            ))
                        TextButton(onClick = {
                            if (languageCode == "ru"){
                                SystemUtil.setLocale(context.getActivity(), "en")
                                SharedPreferencesUtil.setSharedData(context.getActivity(), SharedPreferencesKeys.PREF_LOCALE_LANGUAGE,"en")
                            }
                        }) {
                            Text(text = "En", fontSize = 20.sp, color = if(languageCode=="en") MainColorSecondRed else Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth()){
                Text(modifier = Modifier.align(Alignment.CenterStart), text = stringResource(R.string.enable_notifications), style = MainFont, textAlign = TextAlign.Center)
                Switch(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = checked,
                    colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray),
                    onCheckedChange = { flag ->
                        if (flag)
                            NotificationChannelBuilder.createNotificationChannel(context)
                        else
                            NotificationChannelBuilder.deleteNotificationChannel(context)
                        checked = flag
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
            Box(modifier = boxModifier){
                Text(modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 5.dp), text = stringResource(R.string.start_hour), style = MainFontMedium, textAlign = TextAlign.Center)
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
                        userSettingsViewModel.updateUserSettingsNotificationSleepStart(value)
                    }
                )
            }
            Box(modifier = boxModifier){
                Text(modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 5.dp), text = stringResource(R.string.end_hour), style = MainFontMedium, textAlign = TextAlign.Center)
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
                        userSettingsViewModel.updateUserSettingsNotificationSleepEnd(value)
                    }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            val records = remember {
                mutableStateOf(emptyList<ExcelRecord>())
            }
            val scope = rememberCoroutineScope()
            Button(onClick = {
                val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                val filePath = directory + "/One hour data backup.xls"
                val file = File(filePath)
                val headers = listOf(context.resources.getString(R.string.color), context.resources.getString(R.string.category), context.resources.getString(R.string.activity), context.resources.getString(R.string.date_and_time))
                scope.launch {
                    records.value = activityRecordViewModel.getActivityRecordsForExcel().first()
                    ExcelFileMaker.writeXLSFile(file, records.value, headers)
                }

            }) {
                Row{
                    Icon(imageVector = Icons.Default.TableView, contentDescription = null)
                    Text(stringResource(R.string.import_to_excel))
                }

            }
        }
    }
}
