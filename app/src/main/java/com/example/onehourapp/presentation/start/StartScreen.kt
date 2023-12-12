package com.example.onehourapp.presentation.start

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.onehourapp.R
import com.example.onehourapp.common.preferences.SharedPreferencesKeys
import com.example.onehourapp.notification.NotificationBootReceiverHelper
import com.example.onehourapp.notification.NotificationChannelBuilder

@OptIn(ExperimentalMotionApi::class)
@Composable
fun StartScreen(
    onStartBtnClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val context = LocalContext.current

        var animateToEnd by remember { mutableStateOf(false) }
        val progress by animateFloatAsState(
            targetValue = if (animateToEnd) 1f else 0f,
            animationSpec = tween(1000)
        )
        val motionScene = remember {
            context.resources
                .openRawResource(R.raw.start_motion_scene)
                .readBytes()
                .decodeToString()
        }
        MotionLayout(
            motionScene = MotionScene(content = motionScene),
            progress = progress,
            modifier = Modifier.fillMaxSize()
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .layoutId("image_holder")
            ) {
                val composition: LottieCompositionResult =
                    rememberLottieComposition(LottieCompositionSpec.Asset("logo_anim.json"))
                val animationProgress by animateLottieCompositionAsState(composition.value)

                val infiniteTransition = rememberInfiniteTransition(
                    label = "RotateInfiniteTransition",
                )
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = if (animationProgress == 1f) 360f else 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(60000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "RotateInfiniteTransition",
                )
                Image(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .rotate(rotation)
                        .align(Alignment.Center),
                    painter = painterResource(R.drawable.start_activity_logo_bg),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                )
                LottieAnimation(
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .align(Alignment.Center),
                    composition = composition.value
                )
                LaunchedEffect(animationProgress) {
                    if (animationProgress == 1f) {
                        animateToEnd = true
                    }
                }
            }


            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .layoutId("image_gradient_holder")
            ) {
                val gradientColors = listOf(
                    Color.Transparent,
                    Color.Transparent,
                    Color.Transparent,
                    Color.Transparent,
                    Color.Transparent,
                    Color.Transparent,
                    Color.Black,
                    Color.Black
                )
                drawRect(
                    brush = Brush.verticalGradient(gradientColors),
                    topLeft = Offset(0f, 0f),
                    size = size
                )
            }
            Text(
                text = context.getString(R.string.slogan),
                style = com.example.onehourapp.theme.ui.MainFont,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .layoutId("middle_txt")
            )
            Button(
                onClick = {
                    animateToEnd = !animateToEnd
                    com.example.onehourapp.common.utils.SharedPreferencesUtil.setSharedData(
                        context,
                        SharedPreferencesKeys.PREF_AUTH_STATUS,
                        "true"
                    )
                    NotificationBootReceiverHelper.enableBootReceiver(context)
                    NotificationChannelBuilder.createNotificationChannel(context)
                    onStartBtnClick()
                },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = com.example.onehourapp.theme.ui.MainColorSecondRed,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .layoutId("start_btn")

            ) {
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 50.dp, vertical = 5.dp),
                    color = Color.White,
                    fontSize = 20.sp,
                    text = stringResource(id = R.string.get_start),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}