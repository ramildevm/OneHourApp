package com.example.onehourapp.screens.home.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.onehourapp.helpers.UITextHelper.generateLoremIpsumWords
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.BackgroundSecondColor
import com.example.onehourapp.ui.theme.MainFont


@Composable
fun ActivityScreenContent(navController: NavHostController) {
    RoundedBoxesList()
}
@Composable
fun RoundedBoxesList() {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(BackgroundColor)
        ) {
            items(20) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(Color.Transparent)
                ) {
                    Surface(
                        shape = RoundedCornerShape(15.dp),
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        Column(
                            modifier = Modifier.background(BackgroundSecondColor).padding(5.dp)
                        ) {
                            Text(
                                text = "Item $index",
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                style = MainFont,
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = generateLoremIpsumWords(10),
                                style = MainFont
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

