package com.example.onehourapp.ui.screens.home

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onehourapp.R
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.ui.components.NumberPicker
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.example.onehourapp.utils.CalendarUtil
import com.google.accompanist.pager.ExperimentalPagerApi
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class, ExperimentalComposeUiApi::class,)
@Composable
fun MonthSelectorDialog(
    date: Long,
    hour: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val categoryViewModel:CategoryViewModel = hiltViewModel()
    val activityViewModel:ActivityViewModel = hiltViewModel()
    val activityRecordViewModel:ActivityRecordViewModel = hiltViewModel()

    val mCalendar = Calendar.getInstance()
    mCalendar.timeInMillis = date
    mCalendar.set(Calendar.HOUR_OF_DAY, 0)
    mCalendar.set(Calendar.MINUTE, 0)
    mCalendar.set(Calendar.SECOND, 0)
    mCalendar.set(Calendar.MILLISECOND, 0)

    var selectedDateMillis by remember {
        mutableLongStateOf(date)
    }
    var selectedActivityId by remember { mutableStateOf(-1) }
    var selectedCategoryId by remember { mutableStateOf(-1) }

    var selectedActivityName by remember { mutableStateOf("") }

    var isTextFieldEmpty by rememberSaveable { mutableStateOf(false) }
    var isAddActivity by remember { mutableStateOf(false) }


    var pickerEndValue by remember { mutableIntStateOf(hour) }
    var pickerStartValue by remember { mutableIntStateOf(pickerEndValue-1) }

    AlertDialog(
        backgroundColor = BackgroundColor,
        modifier = Modifier.shadow(15.dp, ambientColor = Color.White, spotColor = Color.LightGray),
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.adding_a_record), textAlign = TextAlign.Center, color= MainColorSecondRed) },
        text = {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = stringResource(R.string.select_a_date),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                val mDate = remember { mutableStateOf(String.format("%02d.%02d.${mCalendar.get(Calendar.YEAR)}", mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.get(Calendar.MONTH)+1)) }

                val mDatePickerDialog = DatePickerDialog(
                    context,
                    { dp: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                        selectedDateMillis = CalendarUtil.getDateTimestamp(mYear, mMonth, mDayOfMonth)
                        mDate.value = String.format("%02d.%02d.$mYear", mDayOfMonth, mMonth+1)
                    }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)
                )
                mDatePickerDialog.datePicker.maxDate = System.currentTimeMillis() + 1000

                Box{
                    OutlinedTextField(value = mDate.value, onValueChange = {}, readOnly = true, trailingIcon = {
                        IconButton(onClick = {  mDatePickerDialog.show() }) {
                            Icon(imageVector = Icons.Default.CalendarMonth, tint = MainColorSecondRed, contentDescription = null)
                        }
                    })
                }

                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = stringResource(R.string.select_an_activity),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))


                val categories = categoryViewModel.allCategories.collectAsState(initial = emptyList())
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }) {
                    Box {
                        if(isAddActivity)
                            OutlinedTextField(
                                label = { Text(text = categories.value.find { category ->  category.id == selectedCategoryId}!!.name) },
                                value = selectedActivityName,
                                onValueChange = { selectedActivityName = it },
                                readOnly = false,
                                keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                                trailingIcon = {
                                    IconButton(onClick = {isAddActivity = false}){Icon(imageVector = Icons.Rounded.Cancel, contentDescription = "Cancel icon")}
                                }
                            )
                        else
                            OutlinedTextField(
                                value = selectedActivityName,
                                onValueChange = {  },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                            )
                    }
                    if(!isAddActivity)
                        ExposedDropdownMenu(
                            modifier = Modifier
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                ),
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.value.forEach { category ->
                                var expandedCategory by remember {
                                    mutableIntStateOf(-1)
                                }
                                val isExpanded = expandedCategory == category.id
                                DropdownMenuItem(
                                    content = {
                                        Box(Modifier.weight(1f)) {
                                            Row {
                                                Text(
                                                    "● ",
                                                    fontSize = 20.sp,
                                                    color = Color(category.color.toColorInt())
                                                )
                                                Text(text = category.name)
                                            }
                                            Text(
                                                text = "▲",
                                                Modifier
                                                    .padding(horizontal = 5.dp)
                                                    .rotate(if (!isExpanded) 180f else 0f)
                                                    .align(Alignment.CenterEnd)
                                            )
                                        }
                                    },
                                    onClick = {
                                        expandedCategory = if (isExpanded) -1 else category.id
                                    }
                                )
                                if (expandedCategory == category.id) {
                                    val activities =
                                        activityViewModel.getActivities(category.id).collectAsState(
                                            initial = emptyList()
                                        )
                                    activities.value.forEach { activity ->
                                        DropdownMenuItem(
                                            content = {
                                                Row(Modifier.padding(horizontal = 10.dp)) {
                                                    Text(
                                                        "● ",
                                                        fontSize = 16.sp,
                                                        color = Color(category.color.toColorInt())
                                                    )
                                                    Text(text = activity.name)
                                                }
                                            },
                                            onClick = {
                                                selectedActivityId = activity.id
                                                selectedCategoryId = -1
                                                selectedActivityName = activity.name
                                                expandedCategory = -1
                                                isAddActivity = false
                                                expanded = false
                                            }
                                        )
                                    }
                                    DropdownMenuItem(
                                        modifier = Modifier.fillMaxWidth(),
                                        content = {
                                            Text(
                                                modifier = Modifier.fillMaxWidth(),
                                                color = MainColorSecondRed,
                                                text = stringResource(id = R.string.add),
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        onClick = {
                                            selectedActivityId = -1
                                            selectedCategoryId = category.id
                                            isAddActivity = true
                                            expanded = false
                                            selectedActivityName = " "
                                        }
                                    )
                                }
                            }
                        }
                }
                if(isTextFieldEmpty){
                    Text(
                        text = stringResource(R.string.empty_field),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption.copy(fontSize = 12.sp),
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                }
                else
                    Spacer(modifier = Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.select_activity_start_hour),
                        softWrap = true,
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .weight(0.5f)
                            .align(Alignment.CenterVertically)
                    )
                    Box(
                        Modifier
                            .weight(0.5f)
                            .height(80.dp)
                            .offset(y=(-10).dp)
                    ) {
                        NumberPicker(
                            modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.CenterEnd),value = pickerStartValue, onValueChange = {pickerStartValue = it},
                            range = 0 until if(pickerEndValue!=0)pickerEndValue else 24,
                            dividersColor = MainColorSecondRed,
                            textStyle = TextStyle.Default.copy(fontSize = 16.sp)
                        )
                    }
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.select_activity_end_hour),
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.onSurface,
                        softWrap = true,
                        modifier = Modifier
                            .weight(0.5f)
                    )
                    Box(
                        Modifier
                            .weight(0.5f)
                            .height(80.dp)
                            .offset(y=(-10).dp)
                            .width(150.dp)
                    ) {
                        NumberPicker(
                            modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.Center),
                            value = pickerEndValue,
                            onValueChange = { pickerEndValue = it
                                if(pickerStartValue>=it)
                                    pickerStartValue = if(it!=0) it-1 else 23},
                            range = 0..(if(selectedDateMillis != date) 23 else hour),
                            dividersColor = MainColorSecondRed,
                            textStyle = TextStyle.Default.copy(fontSize = 16.sp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedActivityName = selectedActivityName.trim()
                    if(selectedActivityName.isEmpty() || selectedActivityName.isBlank()){
                        isTextFieldEmpty = true
                        return@Button
                    }
                    if(isAddActivity){
                        activityViewModel.insertActivity(Activity(0, selectedActivityName,selectedCategoryId))
                        activityViewModel.insertResult.observe(lifecycleOwner) { activityId ->
                            createActivityRecord(selectedDateMillis, pickerStartValue, pickerEndValue, activityRecordViewModel, activityId)
                        }
                    }
                    else{
                        createActivityRecord(selectedDateMillis, pickerStartValue, pickerEndValue, activityRecordViewModel, selectedActivityId)
                    }
                    Toast.makeText(context, context.getText(R.string.successfull_records_add), Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            ) {
                Text(text = stringResource(id = R.string.add))
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss()}, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

fun createActivityRecord(
    selectedDateMillis: Long,
    pickerStartValue: Int,
    pickerEndValue: Int,
    activityRecordViewModel: ActivityRecordViewModel,
    selectedActivityId: Int
) {
    val startDate = Calendar.getInstance()
    startDate.timeInMillis = selectedDateMillis - if(pickerEndValue==0) 86400000 else 0
    startDate.set(Calendar.HOUR_OF_DAY, pickerStartValue)
    startDate.set(Calendar.MINUTE, 0)
    startDate.set(Calendar.SECOND, 0)
    startDate.set(Calendar.MILLISECOND, 0)

    val endDate = Calendar.getInstance()
    endDate.timeInMillis = selectedDateMillis
    endDate.set(Calendar.HOUR_OF_DAY, pickerEndValue)
    endDate.set(Calendar.MINUTE, 0)
    endDate.set(Calendar.SECOND, 0)
    endDate.set(Calendar.MILLISECOND, 0)

    for(timestamp in startDate.timeInMillis until endDate.timeInMillis step 60*60*1000){
        activityRecordViewModel.insertActivityRecord(ActivityRecord(0, selectedActivityId, timestamp))
    }
}

@Composable
fun ComposeAlertDialogExample(date: Long = Calendar.getInstance().timeInMillis, hour:Int,onDismiss: () -> Unit) {
    MonthSelectorDialog(
        date,
        hour,
        onDismiss = onDismiss
    )
}