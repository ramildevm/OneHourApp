package com.example.onehourapp.screens

import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.onehourapp.ui.theme.MainFont
import com.example.onehourapp.R
import com.example.onehourapp.utils.SharedReferencesUtil
import kotlinx.coroutines.delay

@Composable
fun StartContent(
    onClick: () -> Unit){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        MainPanel(onClick)
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun MainPanel(onStartBtnClick: () -> Unit) {
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
        var isImageVisible by remember { mutableStateOf(true) }
        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        Image(
            painter =
            if(isImageVisible) rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = R.drawable.onehour_logo)
                    .apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
            )
            else painterResource(R.drawable.start_activity_main_bg),
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier
                .layoutId("image_holder")

        )
        LaunchedEffect(isImageVisible) {
            if (isImageVisible) {
                delay(2700L)
                animateToEnd  = true
                isImageVisible = false
            }
        }
        Canvas(modifier = Modifier
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
                Color.Black)
            drawRect(
                brush = Brush.verticalGradient(gradientColors),
                topLeft = Offset(0f, 0f),
                size = size
            )
        }
        Text(
            text = context.getString(R.string.slogan),
            style = MainFont,
            modifier = Modifier.layoutId("middle_txt")
        )
        Button(
            onClick = {
                animateToEnd = !animateToEnd
                SharedReferencesUtil.setSharedData(context,"auth_status","true")
                onStartBtnClick()
            },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .layoutId("start_btn")

        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 50.dp, vertical = 5.dp),
                color = Color.Black,
                fontSize = 20.sp,
                text = stringResource(id = R.string.get_start),
                textAlign = TextAlign.Center
            )
        }
    }
}