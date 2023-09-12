package com.example.onehourapp.ui.screens.home.activity

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.onehourapp.R
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.Category
import com.example.onehourapp.ui.helpers.toHexString
import com.example.onehourapp.ui.theme.ActivityListItemFont
import com.example.onehourapp.ui.theme.BackgroundColor
import com.example.onehourapp.ui.theme.CardActivityColor
import com.example.onehourapp.ui.theme.CardCategoryColor
import com.example.onehourapp.ui.theme.MainColorSecondRed
import com.example.onehourapp.ui.theme.TextFieldStyle
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel


@Composable
fun ActivityScreenContent(navController: NavHostController) {
    ActivitiesList()
}

@Composable
fun ActivitiesList() {
    val categoryVM: CategoryViewModel = hiltViewModel()
    val activityVM: ActivityViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    val categories = categoryVM.allCategories.collectAsState(initial = emptyList())

    var isAddBtnClicked by remember { mutableStateOf(false) }

    if(isAddBtnClicked) {
        AddCategoryDialog(
            onConfirm = { name: String, color: Color ->
                categoryVM.insertCategory(Category(0, name, color.toHexString()))
                categoryVM.insertResult.observe(lifecycleOwner) { result ->
                    activityVM.insertActivity(Activity(0, name, result))
                }
                isAddBtnClicked = false
            },
            onDismiss = { isAddBtnClicked = false },
            categories = categories
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = stringResource(id = R.string.activity),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    BackgroundColor
                ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            color = Color.Red
        )
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .weight(1f)
                .background(BackgroundColor)
                .padding(vertical = 4.dp)
        ) {
            itemsIndexed(
                items = categories.value) { _, category ->
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    Card(
                        backgroundColor = Color.DarkGray,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .clickable {
                                expanded = !expanded
                            },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        CategoryCardContent(category, categoryVM, activityVM, expanded) { expanded = !expanded }
                    }

            }
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    elevation = 4.dp) {
                    IconButton(onClick = { isAddBtnClicked = true },  modifier= Modifier.align(Alignment.CenterHorizontally)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)){
                            Text(text = stringResource(R.string.add),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold)
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryCardContent(
    category: Category,
    categoryVM: CategoryViewModel,
    activityVM: ActivityViewModel,
    expanded: Boolean,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Row() {
            Icon(
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp),
                imageVector = Icons.Rounded.Circle,
                contentDescription = "Icon",
                tint = Color(category.color.toColorInt())
            )
            Text(
                text = category.name,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(modifier= Modifier.align(Alignment.CenterVertically),onClick = { onDelete() }) {
                Icon(imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Expand button")
            }
        }

        if (expanded) {
            val activityRecordVM: ActivityRecordViewModel = hiltViewModel()
            val activities = activityVM.getActivities(category.id).collectAsState(initial = emptyList())
            var context = LocalContext.current
            Column( modifier = Modifier.background(CardCategoryColor), content = {
                activities.value.forEach{ activity->
                    var dialogMessage by remember {
                        mutableStateOf("")
                    }
                    var showDialog by remember { mutableStateOf(false) }
                    val isAnyActivityRecord = activityRecordVM.getActivityRecordsCountByActivityId(activity.id) > 0
                    val state = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToStart){
                                if(isAnyActivityRecord)
                                    dialogMessage = context.getString(R.string.delete_message_activity_has_records)
                                if(activities.value.size == 1)
                                    dialogMessage = context.getString(R.string.delete_message_activity_is_single)
                                if(dialogMessage != "")
                                    showDialog = true
                            }
                            dialogMessage == ""
                        }
                    )
                    if(showDialog)
                        DeleteActivityAlertDialog(
                            dialogMessage,
                            onDelete={
                                showDialog = false
                                if(activities.value.size==1)
                                    categoryVM.deleteCategory(category)
                            }
                        ) {
                            showDialog = false
                        }
                    SwipeToDismiss(
                        state = state,
                        background = {
                            val color = when(state.dismissDirection){
                                DismissDirection.EndToStart-> Color.Red
                                DismissDirection.StartToEnd -> Color.Transparent
                                null-> Color.Transparent
                            }
                            Box(
                                Modifier
                                    .padding(20.dp, 4.dp, 8.dp, 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color)
                                    .fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        },
                        directions=setOf(DismissDirection.EndToStart),
                        dismissContent = {
                            Card(
                                elevation = 4.dp,
                                backgroundColor = CardActivityColor,
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(6.dp, 4.dp, 8.dp, 4.dp)
                                        .background(CardActivityColor)
                                ) {
                                    ActivityCardContent(category.color, activity)
                                }
                            }
                        })
                }

                Card(
                    elevation = 4.dp,
                    backgroundColor = CardActivityColor,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CardActivityColor)
                    ) {
                        TextButton(
                            onClick={},
                            modifier = Modifier
                                .fillMaxWidth()){
                            Text(
                                text = stringResource(R.string.add),
                                textAlign= TextAlign.Center,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .weight(1f),
                                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold)
                            )
                        }
                    }
                }
            })
        }
    }
}

@Composable
fun ActivityCardContent(categoryColor:String, activity: Activity) {
    Row(modifier = Modifier.background(CardActivityColor)) {
        Icon(
            modifier = Modifier
                .size(35.dp)
                .padding(vertical = 10.dp),
            imageVector = Icons.Rounded.Circle,
            contentDescription = "Icon",
            tint = Color(categoryColor.toColorInt())
        )
        Text(
            text = activity.name,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            style = ActivityListItemFont
        )
    }
}

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    categories: State<List<Category>>,
    onConfirm: (String, Color) -> Unit
) {
    Dialog(onDismissRequest = onDismiss ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
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

                val usedColors =  categories.value.map {  Color(it.color.toColorInt()) }

                var selectedColor by remember { mutableStateOf<Color?>(null) }
                var text by rememberSaveable { mutableStateOf("") }

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
                        var isError by rememberSaveable { mutableStateOf(false) }

                        fun validate(text: String) {
                            isError = text.isEmpty()
                        }
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(10.dp, 4.dp)
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            value = text,
                            isError = isError,
                            textStyle = TextFieldStyle,
                            onValueChange = { newValue ->
                                text = newValue
                                validate(text)
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = MainColorSecondRed, cursorColor = MainColorSecondRed)
                        )
                        if(isError){
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
                        var isNotSelected by rememberSaveable { mutableStateOf(false) }
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
                                        ColorButton(
                                            color = color,
                                            isUsed = used,
                                            isSelected = selectedColor == color,
                                            alpha = if (used) 0.5f else 1f,
                                            onClick = {
                                                if (used)
                                                    return@ColorButton
                                                if (selectedColor == color) {
                                                    selectedColor = null
                                                } else {
                                                    isNotSelected = false
                                                    selectedColor = color
                                                }
                                            }
                                        )
                                    }
                                    else
                                        Spacer(modifier = Modifier.size(44.dp))
                                }

                            }
                        }
                        if(isNotSelected){
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
                                 if(text.isNotEmpty() && selectedColor!=null){
                                     onConfirm(text, selectedColor!!)
                                 }
                                if(text.isEmpty()){
                                    isError = true
                                }
                                if(selectedColor==null ){
                                    isNotSelected = true
                                }

                            }, Modifier.align(Alignment.CenterStart)) {
                                Text(stringResource(id = R.string.add), fontSize = 16.sp, color = MainColorSecondRed)
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

@Composable
fun DeleteActivityAlertDialog(dialogMessage: String, onDelete: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.warning))
        },
        text = {
            Text(stringResource(R.string.delete_activity_message_confirmation) + "\n" + dialogMessage)
        },
        confirmButton = {
            Button(
                onClick = onDelete
            ) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
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