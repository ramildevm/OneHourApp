package com.example.onehourapp.ui.screens.home

import android.app.DatePickerDialog
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Timeline
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
import com.example.onehourapp.data.models.UserSettings
import com.example.onehourapp.ui.components.NumberPicker
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.example.onehourapp.ui.viewmodels.UserSettingsViewModel
import com.example.onehourapp.utils.CalendarUtil
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.Random


sealed class AddCallerType {
    object HOME_SCREEN : AddCallerType()
    object NOTIFICATION : AddCallerType()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecordDialog(
    date: Long = Calendar.getInstance().timeInMillis,
    hour: Int,
    callerType: AddCallerType,
    onDismiss: () -> Unit,
    notifyChange: (month: Int) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val activityViewModel: ActivityViewModel = hiltViewModel()
    val activityRecordViewModel: ActivityRecordViewModel = hiltViewModel()
    val userSettingsViewModel: UserSettingsViewModel = hiltViewModel()
    var userSettings: UserSettings? by remember {
        mutableStateOf(null)
    }
    LaunchedEffect(key1 = Unit) {
        userSettings = userSettingsViewModel.getUserSettings()
    }
    if (userSettings != null) {
        val actualDate by remember {
            mutableLongStateOf(
                if (userSettings!!.lastAddedDate != 0L)
                    userSettings!!.lastAddedDate
                else {
                    date - if (hour == 0) 86400000L else 0L
                }
            )
        }
        val mCalendar by remember { mutableStateOf(Calendar.getInstance()) }
        mCalendar.timeInMillis = actualDate
        mCalendar.set(Calendar.HOUR_OF_DAY, 0)
        mCalendar.set(Calendar.MINUTE, 0)
        mCalendar.set(Calendar.SECOND, 0)
        mCalendar.set(Calendar.MILLISECOND, 0)

        var selectedDateMillis by remember {
            mutableLongStateOf(actualDate)
        }
        var selectedActivityId by remember { mutableIntStateOf(userSettings!!.lastAddedActivityId) }

        var selectedCategoryId by remember { mutableIntStateOf(-1) }

        var selectedActivityName by remember {
            mutableStateOf(
                activityViewModel.getActivityById(
                    userSettings!!.lastAddedActivityId
                )?.name ?: ""
            )
        }

        var isTextFieldEmpty by rememberSaveable { mutableStateOf(false) }
        var isAddActivity by remember { mutableStateOf(false) }


        var pickerEndValue by remember { mutableIntStateOf(hour) }
        var pickerStartValue by remember { mutableIntStateOf(if (pickerEndValue == 0) 23 else pickerEndValue - 1) }

        AlertDialog(
            backgroundColor = BackgroundColor,
            modifier = Modifier.shadow(
                15.dp,
                ambientColor = Color.White,
                spotColor = Color.LightGray
            ),
            onDismissRequest = {},
            title = {
                Text(
                    text = stringResource(R.string.adding_a_record),
                    textAlign = TextAlign.Center,
                    color = MainColorSecondRed
                )
            },
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

                    val mDate = remember {
                        mutableStateOf(
                            String.format(
                                "%02d.%02d.${mCalendar.get(Calendar.YEAR)}",
                                mCalendar.get(Calendar.DAY_OF_MONTH),
                                mCalendar.get(Calendar.MONTH) + 1
                            )
                        )
                    }

                    val mDatePickerDialog = DatePickerDialog(
                        context,
                        { dp: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                            selectedDateMillis =
                                CalendarUtil.getDateTimestamp(mYear, mMonth, mDayOfMonth)
                            mDate.value = String.format("%02d.%02d.$mYear", mDayOfMonth, mMonth + 1)
                        },
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)
                    )
                    mDatePickerDialog.datePicker.maxDate = when (callerType) {
                        AddCallerType.HOME_SCREEN -> if (hour == 0) selectedDateMillis else System.currentTimeMillis() + 1000L
                        AddCallerType.NOTIFICATION -> selectedDateMillis + 1000L
                    }
                    Box {
                        OutlinedTextField(
                            value = mDate.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { mDatePickerDialog.show() }) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        tint = MainColorSecondRed,
                                        contentDescription = null
                                    )
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
                    //Todo: прогрузка последней выбранной активности и даты создать сущность бд для этого

//                                        val activities = activityViewModel.getActivities().collectAsState(initial = emptyList())
//                                        Button(onClick = { createActivityRecord2(activities, activityRecordViewModel) }) {
//                                            Text("Populate")
//                                        }

                    val categories =
                        categoryViewModel.allCategories.collectAsState(initial = emptyList())
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }) {
                        Box {
                            if (isAddActivity)
                                OutlinedTextField(
                                    label = { Text(text = categories.value.find { category -> category.id == selectedCategoryId }!!.name) },
                                    value = selectedActivityName,
                                    onValueChange = {
                                        if (it.length <= 50) selectedActivityName = it
                                    },
                                    readOnly = false,
                                    keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                                    trailingIcon = {
                                        IconButton(onClick = { isAddActivity = false }) {
                                            Icon(
                                                imageVector = Icons.Rounded.Cancel,
                                                contentDescription = "Cancel icon"
                                            )
                                        }
                                    }
                                )
                            else
                                OutlinedTextField(
                                    value = selectedActivityName,
                                    onValueChange = { },
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded
                                        )
                                    }
                                )
                        }
                        if (!isAddActivity)
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
                                            activityViewModel.getActivities(category.id)
                                                .collectAsState(
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
                    if (isTextFieldEmpty) {
                        Text(
                            text = stringResource(R.string.empty_field),
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption.copy(fontSize = 12.sp),
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    } else
                        Spacer(modifier = Modifier.height(16.dp))
                    var start =
                        if (hour == 0 || selectedDateMillis != date) 23f else (hour - 1).toFloat()
                    var end = if (hour == 0 || selectedDateMillis != date) 24f else hour.toFloat()
                    var sliderPosition by remember { mutableStateOf(start..end) }
                    Box(Modifier.fillMaxWidth()) {
                        Text(stringResource(id = R.string.time))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            text = "${sliderPosition.start.toInt()}  -  ${sliderPosition.endInclusive.toInt()}",
                            textAlign = TextAlign.Center
                        )
                    }
                    RangeSlider(
                        steps = end.toInt() - 1,
                        value = sliderPosition,
                        onValueChange = {
                            sliderPosition = if (it.start < it.endInclusive)
                                it
                            else
                                it.endInclusive - 1..it.endInclusive
                            if (it.start == it.endInclusive && it.endInclusive == 0f)
                                sliderPosition = it.start..it.start + 1
                        },
                        valueRange = 0f..end,
                        onValueChangeFinished = {
                            pickerStartValue = sliderPosition.start.toInt()
                            pickerEndValue = sliderPosition.endInclusive.toInt()
                        }
                    )
                    Row(Modifier.padding(horizontal = 4.dp)) {
                        val step = when (end.toInt()) {
                            in 0 until 12 -> 1
                            in 12 until 18 -> 2
                            in 18..24 -> 3
                            else -> 1
                        }
                        for (i in 0..end.toInt() step step) {
                            Text(String.format("%02d", i))
                            if (i != end.toInt())
                                Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedActivityName = selectedActivityName.trim()
                        if (selectedActivityName.isEmpty() || selectedActivityName.isBlank()) {
                            isTextFieldEmpty = true
                            return@Button
                        }
                        if (isAddActivity) {
                            activityViewModel.insertActivity(
                                Activity(
                                    0,
                                    selectedActivityName,
                                    selectedCategoryId
                                )
                            )
                            activityViewModel.insertResult.observe(lifecycleOwner) { activityId ->
                                createActivityRecord(
                                    hour,
                                    selectedDateMillis,
                                    pickerStartValue,
                                    pickerEndValue,
                                    activityRecordViewModel,
                                    userSettingsViewModel,
                                    activityId
                                )
                            }
                        } else {
                            createActivityRecord(
                                hour,
                                selectedDateMillis,
                                pickerStartValue,
                                pickerEndValue,
                                activityRecordViewModel,
                                userSettingsViewModel,
                                selectedActivityId
                            )
                        }
                        notifyChange(CalendarUtil.getCurrentMonth(selectedDateMillis))
                        Toast.makeText(
                            context,
                            context.getText(R.string.successfull_records_add),
                            Toast.LENGTH_SHORT
                        ).show()
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(id = R.string.add))
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}

fun createActivityRecord2(
    activities: State<List<Activity>>,
    activityRecordViewModel: ActivityRecordViewModel
) {
    val startDate = Calendar.getInstance()
    startDate.timeInMillis = CalendarUtil.getMonthStartMillis(2023, 0)
    val endDate = Calendar.getInstance()
    endDate.timeInMillis = CalendarUtil.getMonthStartMillis(2023, 8)
    var timer = 0
    var id = activities.value[kotlin.random.Random.nextInt(activities.value.size - 1)]
    for (timestamp in startDate.timeInMillis until endDate.timeInMillis step 60 * 60 * 1000) {
        if (timer < kotlin.random.Random.nextInt(7, 11)) {
            if (kotlin.random.Random.nextInt(7) != 6)
                activityRecordViewModel.insertActivityRecord(ActivityRecord(0, id.id, timestamp))
        } else {
            id = activities.value[kotlin.random.Random.nextInt(activities.value.size - 1)]
            timer = 0
        }
        timer++
    }
}

fun createActivityRecord(
    hour: Int,
    selectedDateMillis: Long,
    pickerStartValue: Int,
    pickerEndValue: Int,
    activityRecordViewModel: ActivityRecordViewModel,
    userSettingsViewModel: UserSettingsViewModel,
    selectedActivityId: Int
) {
    val startDate = Calendar.getInstance()
    startDate.timeInMillis = selectedDateMillis
    startDate.set(Calendar.HOUR_OF_DAY, pickerStartValue)
    startDate.set(Calendar.MINUTE, 0)
    startDate.set(Calendar.SECOND, 0)
    startDate.set(Calendar.MILLISECOND, 0)

    val endDate = Calendar.getInstance()
    endDate.timeInMillis = selectedDateMillis + if (pickerEndValue == 24) 85800000 else 0
    endDate.set(Calendar.HOUR_OF_DAY, if (pickerEndValue == 24) 0 else pickerEndValue)
    endDate.set(Calendar.MINUTE, 0)
    endDate.set(Calendar.SECOND, 0)
    endDate.set(Calendar.MILLISECOND, 0)

    for (timestamp in startDate.timeInMillis until endDate.timeInMillis + (if (hour != 0 && pickerEndValue == 24) 60 * 60 * 1000 * 24L else 0L) step 60 * 60 * 1000) {
        activityRecordViewModel.insertActivityRecord(
            ActivityRecord(
                0,
                selectedActivityId,
                timestamp
            )
        )
    }
    userSettingsViewModel.updateUserSettingsAfterRecordAdd(
        UserSettings(
            0,
            selectedActivityId,
            selectedDateMillis
        )
    )
}