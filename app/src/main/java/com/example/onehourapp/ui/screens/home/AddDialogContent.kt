package com.example.onehourapp.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onehourapp.R
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.utils.CalendarUtil

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MonthSelectorDialog(
    day: Int,
    hour: Int,
    months: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var selectedMonth by remember { mutableStateOf(months.first()) }
    var text by remember { mutableStateOf(hour.toString()) }
    var textInputValue by remember { mutableStateOf(TextFieldValue()) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "$day Add dialog", textAlign = TextAlign.Center) },
        text = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Select a Month:",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}) {
                    TextField(
                        value = selectedMonth,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },

                    ) //TODO: CascadeDropDownMenu
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        months.forEach { item ->
                            DropdownMenuItem(
                                content = { Text(text = item) },
                                onClick = {
                                    selectedMonth = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    placeholder = {
                        Text(text = "First name")
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // When "OK" button is clicked, call the onConfirm callback
                    onConfirm(selectedMonth, textInputValue.text)
                    onDismiss()
                }
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
            ) {
                Text(text = "Cancel")
            }
        },
        contentColor = MainColorSecondRed
    )
}

@Composable
fun ComposeAlertDialogExample(day: Int = CalendarUtil.getCurrentDay(), hour:Int,onDismiss: () -> Unit) {
    val months = listOf(
        stringResource(R.string.january),
        stringResource(R.string.february),
        stringResource(R.string.march),
        stringResource(R.string.april),
        stringResource(R.string.may),
        stringResource(R.string.june),
        stringResource(R.string.july),
        stringResource(R.string.august),
        stringResource(R.string.september),
        stringResource(R.string.october),
        stringResource(R.string.november),
        stringResource(R.string.december)
    )
    MonthSelectorDialog(
        day,
        hour,
        months = months,
        onDismiss = onDismiss
    ) { selectedMonth, textInput ->
        onDismiss()
    }
}