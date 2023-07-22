package com.example.onehourapp.screens.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.onehourapp.services.NotificationService
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.MainFont


@Composable
fun SettingsContent(){
    Scaffold(modifier = Modifier.background(BackgroundColor)) {
        val context = LocalContext.current
        Column(Modifier.padding(it).background(BackgroundColor).fillMaxSize()) {
            var checked by remember {
                mutableStateOf(NotificationService().isServiceRunning) //TODO checked state
            }
            Box(modifier = Modifier.fillMaxWidth()){
                Text(modifier = Modifier.align(Alignment.CenterStart), text = "Turn of notifications", style = MainFont, textAlign = TextAlign.Center)
                Switch(modifier = Modifier.align(Alignment.CenterEnd), checked = checked, onCheckedChange = {
                    if(checked){
                        Intent(context, NotificationService::class.java).also {
                            it.action = NotificationService.State.STOP.toString()
                            context.startService(it)
                        }
                    }
                    else{
                        Intent(context, NotificationService::class.java).also {
                            it.action = NotificationService.State.START.toString()
                            context.startService(it)
                        }
                    }
                    checked = !checked
                })

            }

        }
    }
}