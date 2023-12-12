package com.example.onehourapp.settings.presentation

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DriveFolderUpload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.onehourapp.common.GoogleResponseResult
import com.example.onehourapp.common.utils.SystemUtil
import com.example.onehourapp.presentation.components.ClockProgressIndicator
import com.example.onehourapp.presentation.components.NumberPicker
import com.example.onehourapp.settings.domain.repositories.GoogleRepository
import com.example.onehourapp.theme.R
import com.example.onehourapp.theme.ui.BackgroundColor
import com.example.onehourapp.theme.ui.MainColorSecondRed
import com.example.onehourapp.theme.ui.MainFont
import com.example.onehourapp.theme.ui.MainFontMedium
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.delay
import java.util.Collections
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsContent(){
    Scaffold(modifier = Modifier.background(BackgroundColor)) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val settingsViewModel = hiltViewModel<SettingsScreenViewModel>()
        val state = settingsViewModel.state.collectAsState()
        var isSyncing by remember {
            mutableStateOf(false)
        }
        var isSignedIn by remember {
            mutableStateOf(false)
        }
        val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
                    task.addOnSuccessListener {
                        Toast.makeText(context, "Successfully signed in", Toast.LENGTH_LONG).show()
                        isSignedIn = true
                    }
                    .addOnFailureListener{
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Google Login Error!", Toast.LENGTH_LONG).show()
                }
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ){
            Column(
                Modifier
                    .padding(it)
                    .background(BackgroundColor)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize())
            {
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
                                SystemUtil.setLocale(context, "ru")
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
                                SystemUtil.setLocale(context, "en")
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
                        checked = state.value.notificationOnCheck,
                        colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray),
                        onCheckedChange = { isEnabled ->
                            settingsViewModel.onEvent(SettingsScreenEvent.StatusChanged(isEnabled))
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
                            if (!state.value.notificationOnCheck)
                                renderEffect = effect.asComposeRenderEffect()
                        }
                    }
                    .alpha(if (state.value.notificationOnCheck) 1f else 0.65f)
                Box(modifier = boxModifier){
                    Text(modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 5.dp), text = stringResource(R.string.start_hour), style = MainFontMedium, textAlign = TextAlign.Center)
                    NumberPicker(
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterEnd),
                        value = state.value.notificationStartHour,
                        range = 0..23,
                        dividersColor = MainColorSecondRed,
                        textStyle = MainFont,
                        enabled = state.value.notificationOnCheck,
                        onValueChange = { value ->
                            settingsViewModel.onEvent(SettingsScreenEvent.StartHourChanged(value))
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
                        value = state.value.notificationEndHour,
                        range = 1..24,
                        dividersColor = MainColorSecondRed,
                        textStyle = MainFont,
                        enabled = state.value.notificationOnCheck,
                        onValueChange = { value ->
                            settingsViewModel.onEvent(SettingsScreenEvent.EndHourChanged(value))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                val account = GoogleSignIn.getLastSignedInAccount(context)

                if(account!=null || isSignedIn) {
                    Button(onClick = {
                        settingsViewModel.onEvent(SettingsScreenEvent.SyncWithGoogle)
                    }
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.DriveFolderUpload,
                                contentDescription = null
                            )
                            Text(stringResource(R.string.sync))
                        }
                    }
                    LaunchedEffect(key1 = state.value.googleResponseResult){
                        when(state.value.googleResponseResult){
                            GoogleResponseResult.DEFAULT -> {}
                            GoogleResponseResult.SUCCESS -> Toast.makeText(context, "Data synced", Toast.LENGTH_SHORT).show()
                            GoogleResponseResult.ERROR -> Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else
                    Button(onClick = {
                        startForResult.launch(getGoogleSignInClient(context).signInIntent)
                    }) {
                        Row{
                            Icon(imageVector = Icons.Default.DriveFolderUpload, contentDescription = null)
                            Text(stringResource(R.string.backup_with_drive))
                        }
                    }
            }
            if (isSyncing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.5f))
                ) {
                    Row(
                        Modifier
                            .background(Color.Unspecified)
                            .wrapContentSize()
                            .align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(100.dp)) {
                            ClockProgressIndicator(!isSyncing)
                        }
                        Text(
                            text = stringResource(R.string.syncing_please_wait),
                            style = MainFont.copy(color = Color.White),
                            fontSize = 16.sp
                        )

                    }
                }
                LaunchedEffect(Unit){
                    delay(10000)
                    if(isSyncing) {
                        isSyncing = false
                        Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}


private fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(Scope(DriveScopes.DRIVE_FILE), Scope(DriveScopes.DRIVE))
        .build()

    return GoogleSignIn.getClient(context, signInOptions)
}

