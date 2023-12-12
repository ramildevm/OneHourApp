package com.example.onehourapp.dialog.record.presentation

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material.RangeSlider
import androidx.compose.material.TextFieldDefaults

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.ActivityRecord
import com.example.onehourapp.domain.models.UserSettings
import com.example.onehourapp.theme.R
import java.util.Calendar


sealed class AddCallerType {
    object HOMESCREEN : AddCallerType()
    object NOTIFICATION : AddCallerType()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecordDialog(
    date: Long = com.example.onehourapp.common.utils.CalendarUtil.getCurrentDayMillis(),
    hour: Int = com.example.onehourapp.common.utils.CalendarUtil.getCurrentHour(),
    callerType: AddCallerType = AddCallerType.HOMESCREEN,
    onDismiss: () -> Unit,
    notifyChange: (month: Int) -> Unit
) {
    val context = LocalContext.current

    val viewModel = hiltViewModel<AddRecordDialogViewModel>()

    val userSettings: UserSettings = viewModel.getUserSettings()

    LaunchedEffect(key1 = context){
        viewModel.insertRecordsEvents.collect{
            when(it){
                AddRecordDialogViewModel.InsertRecordsResult.SUCCESS -> {
                    onDismiss()
                }
            }
        }
    }

    val lastAddedDate = userSettings.lastAddedDate
    val actualDate by remember {
        when (callerType) {
            AddCallerType.HOMESCREEN ->
                mutableStateOf(
                    if (lastAddedDate != 0L && lastAddedDate <= date)
                        userSettings.lastAddedDate
                    else {
                        date - if (hour == 0) com.example.onehourapp.common.utils.CalendarUtil.DAY_MILLIS else 0L
                    }
                )

            AddCallerType.NOTIFICATION -> mutableStateOf(date)
        }
    }
    val actualHour = if (hour == 0) 24 else if (actualDate != date) 24 else hour
    val mCalendar by remember { mutableStateOf(Calendar.getInstance()) }
    mCalendar.timeInMillis = actualDate

    var selectedDateMillis by remember {
        mutableStateOf(actualDate)
    }


    var selectedCategoryId by remember {
        mutableStateOf(
            viewModel.getActivityById(userSettings.lastAddedActivityId)?.categoryId ?: -1
        )
    }

    var selectedActivityName by remember {
        mutableStateOf(
            viewModel.getActivityById(
                userSettings.lastAddedActivityId
            )?.name ?: ""
        )
    }

    var isTextFieldEmpty by rememberSaveable { mutableStateOf(false) }
    var isAddActivity by remember { mutableStateOf(false) }

    var pickerStartValue by remember { mutableStateOf(actualHour - 1) }
    var pickerEndValue by remember { mutableStateOf(actualHour) }


    AlertDialog(
        backgroundColor = com.example.onehourapp.theme.ui.BackgroundColor,
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
                color = com.example.onehourapp.theme.ui.MainColorSecondRed
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

                val selectedDateString = remember {
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
                    { _, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                        selectedDateMillis =
                            com.example.onehourapp.common.utils.CalendarUtil.getDateTimestamp(
                                mYear,
                                mMonth,
                                mDayOfMonth
                            )
                        selectedDateString.value =
                            String.format("%02d.%02d.$mYear", mDayOfMonth, mMonth + 1)
                    },
                    mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH)
                )
                mDatePickerDialog.datePicker.maxDate = when (callerType) {
                    AddCallerType.HOMESCREEN -> {
                        if (hour == 0) date - com.example.onehourapp.common.utils.CalendarUtil.DAY_MILLIS else date
                    }

                    AddCallerType.NOTIFICATION -> actualDate
                }

                Box {
                    OutlinedTextField(
                        value = selectedDateString.value,
                        colors = TextFieldDefaults.outlinedTextFieldColors(textColor = com.example.onehourapp.theme.ui.InnerTextColor),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            if (callerType == AddCallerType.HOMESCREEN)
                                IconButton(onClick = { mDatePickerDialog.show() }) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        tint = com.example.onehourapp.theme.ui.MainColorSecondRed,
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
//
//                                        val activities = activityViewModel.getActivities().collectAsState(initial = emptyList())
//                                        Button(onClick = { createActivityRecord2(activities, activityRecordViewModel) }) {
//                                            Text("Populate")
//                                        }

                val categories = viewModel.getCategories().collectAsState(emptyList())
                var expanded by remember { mutableStateOf(false) }
                val selectedCategory = categories.value.find { category -> category.id == selectedCategoryId }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }) {
                    Box {
                        if (isAddActivity) {
                            OutlinedTextField(
                                label = { Text(text = selectedCategory!!.name) },
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
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Circle,
                                        null,
                                        tint = Color(selectedCategory!!.color.toColorInt())
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(textColor = com.example.onehourapp.theme.ui.InnerTextColor)
                            )
                        } else
                            OutlinedTextField(
                                leadingIcon = {
                                    selectedCategory?.let {
                                        Icon(
                                            Icons.Default.Circle,
                                            null,
                                            tint = Color(selectedCategory.color.toColorInt())
                                        )
                                    }
                                },
                                value = selectedActivityName,
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = expanded
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(textColor = com.example.onehourapp.theme.ui.InnerTextColor)
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
                                    mutableStateOf(-1)
                                }
                                val isExpanded = expandedCategory == category.id
                                DropdownMenuItem(
                                    content = {
                                        Column(Modifier.weight(1f)) {
                                            Row {
                                                Text(
                                                    "● ",
                                                    fontSize = 20.sp,
                                                    color = Color(category.color.toColorInt())
                                                )
                                                Text(text = category.name, Modifier.weight(1f))
                                                Text(
                                                    text = "▲",
                                                    Modifier
                                                        .padding(horizontal = 5.dp)
                                                        .rotate(if (!isExpanded) 180f else 0f)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        expandedCategory = if (isExpanded) -1 else category.id
                                    }
                                )
                                if (expandedCategory == category.id) {
                                    val activities =
                                        viewModel.getActivitiesByCategoryId(category.id)
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
                                                viewModel.onEvent(AddRecordDialogEvent.SelectedActivityIdChanged(activity.id))
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
                                                color = com.example.onehourapp.theme.ui.MainColorSecondRed,
                                                text = stringResource(id = R.string.add),
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        onClick = {
                                            viewModel.onEvent(AddRecordDialogEvent.SelectedActivityIdChanged(-1))
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
                key(selectedDateMillis.hashCode()) {
                    println("Date selected  ms $selectedDateMillis  $date")
                    println(
                        "Date selected ${
                            com.example.onehourapp.common.utils.CalendarUtil.getCurrentDay(
                                selectedDateMillis
                            )
                        }  ${com.example.onehourapp.common.utils.CalendarUtil.getCurrentHour(date)}"
                    )
                    val start = if (selectedDateMillis != date) 23f else (hour - 1).toFloat()
                    val end = if (selectedDateMillis != date) 24f else hour.toFloat()
                    var sliderPosition by remember { mutableStateOf(start..end) }
                    Box(Modifier.fillMaxWidth()) {
                        Text(
                            stringResource(id = R.string.time),
                            color = Color.White
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            text = "${sliderPosition.start.toInt()}  -  ${sliderPosition.endInclusive.toInt()}",
                            textAlign = TextAlign.Center
                        )
                    }
                    RangeSlider(
                        steps = (end - 1f).toInt(),
                        value = sliderPosition,
                        enabled = end != 1f,
                        onValueChange = {
                            sliderPosition = if (it.start < it.endInclusive)
                                it
                            else if (it.start == it.endInclusive && it.start != end)
                                it.start..(it.start + 1)
                            else
                                (it.endInclusive - 1)..it.endInclusive
                            if (it.start == it.endInclusive && it.endInclusive == 0f)
                                sliderPosition = it.start..(it.start + 1)
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
                            Text(
                                String.format("%02d", i),
                                color = com.example.onehourapp.theme.ui.InnerTextColor
                            )
                            if (i != end.toInt())
                                Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                }
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
                    val activity = if (isAddActivity) Activity(
                        0,
                        selectedActivityName.trim(),
                        selectedCategoryId
                    ) else null
                    viewModel.onEvent(AddRecordDialogEvent.OnAdd(
                        selectedDateMillis,
                        pickerStartValue,
                        pickerEndValue,
                        activity))

                    notifyChange(
                        com.example.onehourapp.common.utils.CalendarUtil.getCurrentMonth(
                            selectedDateMillis
                        )
                    )
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

/*
.
fun createActivityRecord3(
    activities: State<List<Activity>>,
    activityRecordViewModel: ActivityRecordViewModel
) {
    val startDate = Calendar.getInstance()
    startDate.timeInMillis = com.example.onehourapp.common.utils.CalendarUtil.getMonthStartMillis(2022, 0)
    val endDate = Calendar.getInstance()
    endDate.timeInMillis = com.example.onehourapp.common.utils.CalendarUtil.getMonthStartMillis(2023, 0)
    //var id = activities.value[kotlin.random.Random.nextInt(activities.value.size - 1)]
    var day = 0
    for (timestamp in startDate.timeInMillis until endDate.timeInMillis step 60 * 60 * 1000 * 24) {
        //Log.e("Inserted", "day: $day -----------------")
        day++
        var hour = 0
        val h1 = kotlin.random.Random.nextInt(5,8)
        val h2 = kotlin.random.Random.nextInt(0,2)
        val h3 = kotlin.random.Random.nextInt(6,9)
        val h4 = kotlin.random.Random.nextInt(2,4)
        val h5 = kotlin.random.Random.nextInt(1,3)
        val h6 = kotlin.random.Random.nextInt(1,3)
        var hourList = listOf<Int>(
            h1, //7 slee
            h2, //1 excer
            h3, //8 work
            h4, //3 famil
            h5, //2 otd
            h6, //2 read
        )
        for(i in 0 until hourList[0]){
            activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 1, hour * 60 * 60 * 1000 + timestamp), hour, day)
            hour ++
        }
        for(i in 0 until hourList[1]){
            activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 7, hour * 60 * 60 * 1000 + timestamp), hour, day)
            hour ++
        }
        for(i in 0 until hourList[2]){
            activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 9, hour * 60 * 60 * 1000 + timestamp), hour, day)
            hour ++
        }
        for(i in 0 until hourList[3]){
            activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 5, hour * 60 * 60 * 1000 + timestamp), hour, day)
            hour ++
        }
        for(i in 0 until hourList[4]){
            activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 3, hour * 60 * 60 * 1000 + timestamp), hour, day)
            hour ++
        }
        for(i in 0 until hourList[5]){
            activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 6, hour * 60 * 60 * 1000 + timestamp), hour, day)
            hour ++
        }
        for(i in 0 until (23 - hourList.sum())){
            activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 8, hour * 60 * 60 * 1000 + timestamp), hour, day)
            hour ++
        }
        activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 1, hour * 60 * 60 * 1000 + timestamp), hour, day)


    }
}
fun createActivityRecord4(
    activities: State<List<Activity>>,
    activityRecordViewModel: ActivityRecordViewModel
) {
    val startDate = Calendar.getInstance()
    startDate.timeInMillis = com.example.onehourapp.common.utils.CalendarUtil.getMonthStartMillis(2022, 0)
    val endDate = Calendar.getInstance()
    endDate.timeInMillis = com.example.onehourapp.common.utils.CalendarUtil.getMonthStartMillis(2023, 0)
    //var id = activities.value[kotlin.random.Random.nextInt(activities.value.size - 1)]
    var day = 0
//    for (timestamp in startDate.timeInMillis until endDate.timeInMillis step 60 * 60 * 1000 * 24) {
//        if(kotlin.random.Random.nextBoolean())
//            activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 2, 21 * 60 * 60 * 1000 + timestamp))
//        if(kotlin.random.Random.nextBoolean())
//            activityRecordViewModel.insertActivityRecord(ActivityRecord(0, 2, 20 * 60 * 60 * 1000 + timestamp))
//
//    }
    for (timestamp2 in startDate.timeInMillis until endDate.timeInMillis step 60 * 60 * 1000 * 24 * 7) {
        for(i in 0..1) {
            val timestamp = timestamp2 + (60 * 60 * 1000 * 24 * i)
            var hour = 0
            if (i == 1) {
                val h1 = kotlin.random.Random.nextInt(7, 10)
                val h2 = 1
                val h3 = kotlin.random.Random.nextInt(4, 6)
                val h4 = kotlin.random.Random.nextInt(3, 6)
                val h5 = kotlin.random.Random.nextInt(1, 4)
                val hourList = listOf<Int>(
                    h1, //9 slee
                    h2, //1 excer
                    h3, //5 family
                    h4, //5 otd
                    h5, //3 read
                )
                for (k in 0 until hourList[0]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            1,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (k in 0 until hourList[1]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            7,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until hourList[2]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            5,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until hourList[3]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            3,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until hourList[4]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            6,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until (23 - hourList.sum())) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            8,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                if (kotlin.random.Random.nextBoolean())
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            1,
                            22 * 60 * 60 * 1000 + timestamp
                        )
                    )
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        1,
                        hour * 60 * 60 * 1000 + timestamp
                    ), hour, day
                )
            }
            else{
                val h1 = kotlin.random.Random.nextInt(7, 9)
                val h2 = 1
                val h3 = kotlin.random.Random.nextInt(3, 5)
                val h4 = kotlin.random.Random.nextInt(5, 8)
                val h5 = 2
                val h6 = 1
                var hourList = listOf<Int>(
                    h1, //8 slee
                    h2, //1 excer
                    h3, //4 family
                    h4, //7 friends
                    h5, //2 otd
                    h5, //1 read
                    h6
                )
                for (i in 0 until hourList[0]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            1,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until hourList[1]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            7,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until hourList[2]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            5,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until hourList[3]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            4,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until hourList[4]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            3,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until hourList[5]) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            6,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                for (i in 0 until (23 - hourList.sum())) {
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            3,
                            hour * 60 * 60 * 1000 + timestamp
                        ), hour, day
                    )
                    hour++
                }
                if (kotlin.random.Random.nextBoolean())
                    activityRecordViewModel.insertActivityRecord(
                        ActivityRecord(
                            0,
                            1,
                            22 * 60 * 60 * 1000 + timestamp
                        )
                    )
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        1,
                        hour * 60 * 60 * 1000 + timestamp
                    ), hour, day
                )
            }
        }
    }
}
fun createActivityRecord2(
    activities: State<List<Activity>>,
    activityRecordViewModel: ActivityRecordViewModel
) {
    val startDate = Calendar.getInstance()
    startDate.timeInMillis = com.example.onehourapp.common.utils.CalendarUtil.getMonthStartMillis(2022, 4)
    val endDate = Calendar.getInstance()
    endDate.timeInMillis = com.example.onehourapp.common.utils.CalendarUtil.getMonthStartMillis(2022, 5)
    //var id = activities.value[kotlin.random.Random.nextInt(activities.value.size - 1)]
    var day = 0
    for (timestamp in startDate.timeInMillis until endDate.timeInMillis step 60 * 60 * 1000 * 24) {
        day++
        if(day in 1..8) {
            var hour = 0
            val h1 = kotlin.random.Random.nextInt(7, 9)
            val h2 = 1
            val h3 = kotlin.random.Random.nextInt(2, 3)
            val h4 = kotlin.random.Random.nextInt(5, 10)
            val h5 = 2
            val h6 = 1
            var hourList = listOf<Int>(
                h1, //8 slee
                h2, //1 excer
                h3, //4 family
                h4, //7 friends
                h5, //2 otd
                h5, //1 read
                h6
            )
            for (i in 0 until hourList[0]) {
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        1,
                        hour * 60 * 60 * 1000 + timestamp
                    ), hour, day
                )
                hour++
            }
            for (i in 0 until hourList[1]) {
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        7,
                        hour * 60 * 60 * 1000 + timestamp
                    ), hour, day
                )
                hour++
            }
            for (i in 0 until hourList[2]) {
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        5,
                        hour * 60 * 60 * 1000 + timestamp
                    ), hour, day
                )
                hour++
            }
            for (i in 0 until hourList[3]) {
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        4,
                        hour * 60 * 60 * 1000 + timestamp
                    ), hour, day
                )
                hour++
            }
            for (i in 0 until hourList[4]) {
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        3,
                        hour * 60 * 60 * 1000 + timestamp
                    ), hour, day
                )
                hour++
            }
            for (i in 0 until hourList[5]) {
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        6,
                        hour * 60 * 60 * 1000 + timestamp
                    ), hour, day
                )
                hour++
            }
            for (i in 0 until (23 - hourList.sum())) {
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        3,
                        hour * 60 * 60 * 1000 + timestamp
                    ), hour, day
                )
                hour++
            }
            if (kotlin.random.Random.nextBoolean())
                activityRecordViewModel.insertActivityRecord(
                    ActivityRecord(
                        0,
                        1,
                        22 * 60 * 60 * 1000 + timestamp
                    )
                )
            activityRecordViewModel.insertActivityRecord(
                ActivityRecord(
                    0,
                    1,
                    hour * 60 * 60 * 1000 + timestamp
                ), hour, day
            )
        }
    }


}
*/