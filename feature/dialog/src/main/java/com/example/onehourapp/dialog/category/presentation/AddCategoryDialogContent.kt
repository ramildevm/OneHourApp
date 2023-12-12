package com.example.onehourapp.dialog.category.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onehourapp.common.ValidationEvent
import com.example.onehourapp.domain.models.Category
import com.example.onehourapp.theme.ui.BackgroundColor
import com.example.onehourapp.theme.ui.MainColorSecondRed
import com.example.onehourapp.theme.ui.TextFieldStyle
import com.example.onehourapp.theme.R


@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss ) {
        val viewModel = hiltViewModel<AddCategoryDialogViewModel>()
        val state = viewModel.state.collectAsState()
        val context = LocalContext.current
        LaunchedEffect(key1 = context){
            viewModel.validationEvents.collect {
                when (it) {
                    ValidationEvent.SUCCESS -> {
                        onDismiss()
                    }
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .shadow(
                    30.dp,
                    RoundedCornerShape(16.dp),
                    ambientColor = Color.White,
                    spotColor = Color.Gray
                ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(BackgroundColor)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                val colors = listOf(
                    Color(0xFFFF0000), Color(0xFFF84D2B), Color(0xFFFF8F2C),
                    Color(0xFFECFF00), Color(0xFF8FFF00), Color(0xFF01A92D),
                    Color(0xFF005B18), Color(0xFF00FF85), Color(0xFF6CFFC1),
                    Color(0xFF9DD1FF), Color(0xFF4784ED), Color(0xFF092F79),
                    Color(0xFF082152), Color(0xFF2A0084), Color(0xFF590070),
                    Color(0xFFB100B5), Color(0xFFFF9BFC), Color(0xFFFF00B8)
                )

                val usedColors =  state.value.categories.map {  Color(it.color.toColorInt()) }

                Box(
                    Modifier
                        .padding(5.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .background(BackgroundColor)) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(BackgroundColor),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.add_category),
                            style = TextFieldStyle.copy(fontWeight = FontWeight.Bold, color = MainColorSecondRed),
                            modifier = Modifier.padding(20.dp, 4.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(id = R.string.input_name),
                            style = TextFieldStyle,
                            modifier = Modifier.padding(10.dp, 0.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(10.dp, 4.dp)
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            value = state.value.name,
                            isError = state.value.nameError!=null,
                            textStyle = TextFieldStyle,
                            onValueChange = { newValue ->
                                viewModel.onEvent(AddCategoryDialogEvent.NameChanged(newValue))
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = MainColorSecondRed, cursorColor = MainColorSecondRed)
                        )
                        if(state.value.nameError!=null){
                            Text(
                                text = stringResource(R.string.empty_field),
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(18.dp)
                            )
                        }
                        else
                            Spacer(Modifier.height(18.dp))
                        Text(
                            text = stringResource(R.string.select_a_color),
                            style = TextFieldStyle,
                            modifier = Modifier.padding(10.dp, 0.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        repeat(4) { rowIndex ->
                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(vertical = 1.dp)
                                    .wrapContentWidth(),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(5) { columnIndex ->
                                    val index = rowIndex * 5 + columnIndex
                                    val color = colors.getOrNull(index)
                                    var used = false
                                    if (usedColors.find { it == color } != null)
                                        used = true

                                    if (color != null) {
                                        val isSelected = state.value.selectedColor == color
                                        ColorButton(
                                            color = color,
                                            isUsed = used,
                                            isSelected = isSelected,
                                            alpha = if (used) 0.5f else 1f,
                                            onClick = {
                                                if (used)
                                                    return@ColorButton
                                                if (isSelected) {
                                                    viewModel.onEvent(AddCategoryDialogEvent.SelectedColorChanged(null))
                                                } else {
                                                    viewModel.onEvent(AddCategoryDialogEvent.SelectedColorChanged(color))
                                                }
                                            }
                                        )
                                    }
                                    else
                                        Spacer(modifier = Modifier.size(44.dp))
                                }

                            }
                        }
                        if(state.value.colorError){
                            Text(
                                text = stringResource(R.string.color_is_not_selected),
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(18.dp)
                            )
                        }
                        else
                            Spacer(Modifier.height(18.dp))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)) {
                            TextButton(onClick = {
                                    viewModel.onEvent(AddCategoryDialogEvent.OnConfirm)
                                },
                                Modifier.align(Alignment.CenterStart)) {
                                Text(
                                    stringResource(id = R.string.add),
                                    fontSize = 16.sp,
                                    color = MainColorSecondRed
                                )
                            }
                            TextButton(onClick = onDismiss, Modifier.align(Alignment.CenterEnd)) {
                                Text(
                                    stringResource(id = R.string.cancel),
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
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
            color = if (isSelected || isUsed) if (isUsed) Color.Gray else color else Color.Transparent,
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