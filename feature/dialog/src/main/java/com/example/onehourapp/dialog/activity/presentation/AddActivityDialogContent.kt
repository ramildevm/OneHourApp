package com.example.onehourapp.dialog.activity.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.onehourapp.theme.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddActivityDialog(
    onDismiss: () -> Unit,
    category: Category
) {
    Dialog(onDismissRequest = onDismiss ) {
        val viewModel = hiltViewModel<AddActivityDialogViewModel>().apply {
            onEvent(AddActivityDialogEvent.SelectedCategoryChanged(category))
        }
        val state = viewModel.state
        val context = LocalContext.current
        LaunchedEffect(key1 = context){
            viewModel.validationEvents.collect{
                when(it){
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
                    .background(com.example.onehourapp.theme.ui.BackgroundColor)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                var expanded by remember { mutableStateOf(false) }
                Box(
                    Modifier
                        .padding(5.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .background(com.example.onehourapp.theme.ui.BackgroundColor)) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(com.example.onehourapp.theme.ui.BackgroundColor),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.add_activity),
                            style = com.example.onehourapp.theme.ui.TextFieldStyle.copy(fontWeight = FontWeight.Bold, color = com.example.onehourapp.theme.ui.MainColorSecondRed),
                            modifier = Modifier.padding(20.dp, 4.dp)
                        )
                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = stringResource(R.string.category)+":",
                            style = com.example.onehourapp.theme.ui.TextFieldStyle,
                            modifier = Modifier.padding(14.dp, 0.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        /*ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}, modifier = Modifier.padding(horizontal = 10.dp)) {
                            TextField(
                                value = state.selectedCategory.name,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                leadingIcon = {Icon(Icons.Default.Circle ,null, tint=  Color(state.selectedCategory.color.toColorInt()))}
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                viewModel.categories.collectAsState(initial = emptyList()).value.forEach { currentCategory ->
                                    DropdownMenuItem(
                                        content = {
                                            Row {
                                                Text(
                                                    "● ",
                                                    fontSize = 20.sp,
                                                    color = Color(currentCategory.color.toColorInt())
                                                )
                                                Text(text = currentCategory.name, Modifier.weight(1f))
                                            }
                                        },
                                        onClick = {
                                            viewModel.onEvent(
                                                AddActivityDialogEvent.SelectedCategoryChanged(
                                                    currentCategory
                                                )
                                            )
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                         */
                                        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

                                            var exposed by remember { mutableStateOf(false) }
                                            var text by remember { mutableStateOf("") }
                                            ExposedDropdownMenuBox(
                                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                                expanded = exposed,
                                                onExpandedChange = { exposed = !exposed }) {
                                                TextField(
                                                    value = text,
                                                    onValueChange = { text = it })
                                                ExposedDropdownMenu(
                                                    expanded = exposed,
                                                    onDismissRequest = { exposed = false }) {
                                                    for (i in 0..3) {

                                                        DropdownMenuItem(onClick = {
                                                            text = i.toString()
                                                        }) {
                                                            Text(i.toString())
                                                        }
                                                    }
                                                }
                                            }
                                        }
                        Text(
                            text = stringResource(id = R.string.input_name),
                            style = com.example.onehourapp.theme.ui.TextFieldStyle,
                            modifier = Modifier.padding(14.dp, 0.dp)
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .padding(10.dp, 4.dp)
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            value = state.name,
                            isError = state.nameError != null,
                            textStyle = com.example.onehourapp.theme.ui.TextFieldStyle,
                            onValueChange = { newValue ->
                                viewModel.onEvent(AddActivityDialogEvent.NameChanged(newValue))
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = com.example.onehourapp.theme.ui.MainColorSecondRed, cursorColor = com.example.onehourapp.theme.ui.MainColorSecondRed)
                        )
                        state.nameError?.let{
                            Text(
                                text = it,
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(18.dp)
                            )
                        }?: Spacer(Modifier.height(16.dp))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)) {
                            TextButton(
                                onClick = {viewModel.onEvent(AddActivityDialogEvent.OnConfirm)},
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                    Text(stringResource(id = R.string.add), fontSize = 16.sp, color = com.example.onehourapp.theme.ui.MainColorSecondRed)
                            }
                            TextButton(onClick = onDismiss, Modifier.align(Alignment.CenterEnd)) {
                                Text(stringResource(id = R.string.cancel), fontSize = 16.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}