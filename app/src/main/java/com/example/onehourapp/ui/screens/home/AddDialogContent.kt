package com.example.onehourapp.ui.screens.home

import android.app.Activity
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onehourapp.R
import com.example.onehourapp.ui.helpers.fadingEdge
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.example.onehourapp.utils.CalendarUtil
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.forEach
import me.saket.cascade.rememberCascadeState
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class, ExperimentalComposeUiApi::class,)
@Composable
fun MonthSelectorDialog(
    day: Int,
    hour: Int,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    val mCalendar = Calendar.getInstance()

    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDate = remember { mutableStateOf("") }
    mDate.value = "$mDay/${mMonth+1}/$mYear"

    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )
    mDatePickerDialog.datePicker.maxDate = System.currentTimeMillis() + 1000

    val categoryViewModel:CategoryViewModel = hiltViewModel()
    val activityViewModel:ActivityViewModel = hiltViewModel()

    var selectedActivityId by remember { mutableStateOf(0) }
    var selectedHour by remember { mutableStateOf(hour) }

    AlertDialog(
        backgroundColor = BackgroundColor,
        onDismissRequest = {},
        title = { Text(text = "$selectedHour Add dialog", textAlign = TextAlign.Center) },
        text = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Select a date:",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .clickable {
                            mDatePickerDialog.show()
                        }
                        .border(
                            1.5.dp,
                            Color.Gray,
                            RoundedCornerShape(5.dp)
                        )
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center) {
                    Text(text = mDate.value)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Select an activity:",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                val categories =
                    categoryViewModel.allCategories.collectAsState(initial = emptyList())

                var selectedCategoryName by remember { mutableStateOf("") }
                var selectedActivityName by remember { mutableStateOf("") }
                var isAddActivity by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }) {
                    Box {
                        if(isAddActivity)
                            OutlinedTextField(
                                label = { Text(text = selectedCategoryName) },
                                value = selectedActivityName,
                                onValueChange = { selectedActivityName = it },
                                readOnly = false,
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
                                            selectedCategoryName = category.name
                                            isAddActivity = true
                                            expanded = false
                                            selectedActivityName = " "
                                        }
                                    )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                val initialHour = Int.MAX_VALUE / 2 - 14 + selectedHour -1
                val pagerState = rememberPagerState(initialPage = initialHour)

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Select end of activity hour:",
                        softWrap = true,
                        modifier = Modifier.weight(0.5f)
                    )
                    Box(
                        Modifier
                            .weight(0.5f)
                            .height(50.dp)
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            count = Int.MAX_VALUE,
                            modifier = Modifier
                                .fillMaxWidth()
                                .width(100.dp)
                                .background(Color.Transparent),
                            itemSpacing = (-20).dp,
                            contentPadding = PaddingValues(horizontal = 20.dp)
                        ) { pageIndex ->
                            val pageNum = pageIndex % 24
                            val pageText = String.format("%02d", pageNum)
                            Column(
                                Modifier
                                    .padding(5.dp)
                                    .alpha(if (pageIndex != pagerState.currentPage) 0.5f else 1f)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(if (pageIndex == pagerState.currentPage) MainColorSecondRed else Color.DarkGray)
                            ) {
                                Text(
                                    text = pageText, fontSize = 20.sp, modifier = Modifier
                                        .padding(vertical = 3.dp, horizontal = 10.dp)
                                )

                            }

                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 5.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            BackgroundColor,
                                            Color.Transparent,
                                            Color.Transparent,
                                            Color.Transparent,
                                            BackgroundColor
                                        )
                                    )
                                )
                        )

                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm("", "") //todo: remove spaces
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
        }
    )
}

@Composable
fun ComposeAlertDialogExample(day: Int = CalendarUtil.getCurrentDay(), hour:Int,onDismiss: () -> Unit) {
    MonthSelectorDialog(
        day,
        hour,
        onDismiss = onDismiss
    ) { _, _ ->
        onDismiss()
    }
}