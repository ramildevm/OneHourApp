package com.example.onehourapp.ui.screens.home.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.onehourapp.ui.helpers.UITextHelper.generateLoremIpsumWords
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.BackgroundSecondColor
import com.example.onehourapp.ui.theme.MainFont
import com.example.onehourapp.ui.viewmodels.CategoryViewModel


@Composable
fun ActivityScreenContent(navController: NavHostController) {
    RoundedBoxesList()
}
@Composable
fun RoundedBoxesList() {
    val categoryVM: CategoryViewModel = hiltViewModel()
    val categories =  categoryVM.allCategories.collectAsState(initial = emptyList())
    Column(modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center) {
        ColorGrid()
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(BackgroundColor)
        ) {
            itemsIndexed(categories.value) { index, category ->
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
                            modifier = Modifier
                                .background(BackgroundSecondColor)
                                .padding(5.dp)
                        ) {
                            Text(
                                text = "Item $index",
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                style = MainFont,
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = category.name,
                                style = MainFont,
                                color = Color(category.color.toColorInt())
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}
@Composable
fun ColorGrid() {
    val colors = listOf(
        Color(0xFFFF0000), Color(0xFFF84D2B), Color(0xFFFF8F2C),
        Color(0xFFECFF00), Color(0xFF8FFF00), Color(0xFF01A92D),
        Color(0xFF005B18), Color(0xFF00FF85), Color(0xFF6CFFC1),
        Color(0xFF9DD1FF), Color(0xFF4784ED), Color(0xFF092F79),
        Color(0xFF082152), Color(0xFF2A0084), Color(0xFF590070),
        Color(0xFFB100B5), Color(0xFFFF9BFC), Color(0xFFFF00B8)
    )
    val usedColors = listOf(
        Color(0xFFFF0000),Color(0xFF092F79),Color(0xFF8FFF00),
        Color(0xFF4784ED),Color(0xFF590070),Color(0xFFFF9BFC),
        Color(0xFFFF00B8) )
    val selectedColor = remember { mutableStateOf<Color?>(null) }

    Column(
        modifier = Modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(6) { columnIndex ->
                    val index = rowIndex * 6 + columnIndex
                    val color = colors.getOrNull(index)
                    var used = false

                    if(usedColors.find { it == color }!=null)
                        used = true

                    if (color != null) {
                        ColorButton(
                            color = color,
                            isUsed = used,
                            isSelected = selectedColor.value == color,
                            alpha = if(used) 0.5f else 1f,
                            onClick = {
                                if(used)
                                    return@ColorButton
                                if (selectedColor.value == color) {
                                    selectedColor.value = null
                                } else {
                                    selectedColor.value = color
                                }
                            }
                        )
                    } else {
                        Spacer(modifier = Modifier.size(48.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ColorButton(color: Color, isUsed: Boolean, isSelected: Boolean, alpha:Float, onClick: () -> Unit) {
    Box(modifier = Modifier
        .size(44.dp)
        .alpha(alpha)
        .clip(CircleShape)
        .background(Color.Transparent)
        .border(
            width = 2.5.dp,
            color = if (isSelected || isUsed) if (isUsed) Color.DarkGray else color else Color.Transparent,
            shape = CircleShape
        )
        .clickable { onClick() },
        contentAlignment = Alignment.Center){
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .size(34.dp)
            .clip(CircleShape)
            .background(color = color)
    )
    }

}
