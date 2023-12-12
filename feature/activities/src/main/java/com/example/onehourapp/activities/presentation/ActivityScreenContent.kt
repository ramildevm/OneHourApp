package com.example.onehourapp.activities.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.Category
import com.example.onehourapp.theme.R
import com.example.onehourapp.dialog.activity.presentation.AddActivityDialog


@Composable
fun ActivityScreenContent(navController: NavHostController) {
    val viewModel = hiltViewModel<ActivitiesScreenViewModel>()
    val state = viewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(com.example.onehourapp.theme.ui.BackgroundColor),
        verticalArrangement = Arrangement.Center
    ){
        Box(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.activities),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .background(
                        com.example.onehourapp.theme.ui.BackgroundColor
                    ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                color = Color.Red
            )
            var expandedCascadeList by remember {
                mutableStateOf(false)
            }
            IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = {expandedCascadeList=true  }) {
                Icon(imageVector = Icons.Rounded.Sort, contentDescription = null)
            }
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterEnd)) {
                me.saket.cascade.CascadeDropdownMenu(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .shadow(10.dp, ambientColor = Color.Black),
                    expanded = expandedCascadeList,
                    onDismissRequest = { expandedCascadeList = false }) {
                    DropdownMenuItem(
                        onClick = {
                            viewModel.onEvent(ActivitiesScreenEvent.SortTypeChanged(CategorySortType.DEFAULT))
                            expandedCascadeList = false
                                  },
                        modifier = Modifier
                            .background(com.example.onehourapp.theme.ui.SortItemColor)
                            .align(Alignment.End)
                    ) {
                        Row {
                            Icon(imageVector = Icons.Default.Sort, contentDescription = null)
                            Text("By default", Modifier.padding(horizontal = 10.dp))
                        }
                    }
                    DropdownMenuItem(
                        onClick = {
                            viewModel.onEvent(ActivitiesScreenEvent.SortTypeChanged(CategorySortType.NAME))
                            expandedCascadeList = false
                        },
                        modifier = Modifier
                            .background(com.example.onehourapp.theme.ui.SortItemColor)
                            .align(Alignment.End)
                    ) {
                        Row {
                            Icon(imageVector = Icons.Default.Sort, contentDescription = null)
                            Text("By name", Modifier.padding(horizontal = 10.dp))
                        }
                    }
                    DropdownMenuItem(
                        onClick = {
                            viewModel.onEvent(ActivitiesScreenEvent.SortTypeChanged(CategorySortType.COLOR))
                            expandedCascadeList = false
                        },
                        modifier = Modifier
                            .background(com.example.onehourapp.theme.ui.SortItemColor)
                            .align(Alignment.End)
                    ) {
                        Row {
                            Icon(imageVector = Icons.Default.Sort, contentDescription = null)
                            Text("By color", Modifier.padding(horizontal = 10.dp))
                        }
                    }
                }
            }
        }
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .weight(1f)
                .background(com.example.onehourapp.theme.ui.BackgroundColor)
                .padding(vertical = 4.dp)
        ) {
            itemsIndexed(items = state.value.categories) { _, category ->
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
                    CategoryCardContent(category, expanded, viewModel) { expanded = !expanded }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryCardContent(
    category: Category,
    expanded: Boolean,
    viewModel: ActivitiesScreenViewModel = hiltViewModel(),
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
        Row {
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
                style = com.example.onehourapp.theme.ui.CategoryListItemFont
            )
            IconButton(modifier= Modifier.align(Alignment.CenterVertically),onClick = { onDelete() }) {
                Icon(imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Expand button")
            }
        }

        if (expanded) {
            val activities = viewModel.getActivitiesByCategoryId(categoryId = category.id).collectAsState(
                initial = emptyList()
            )
            var isAddActivityDialogShowed by remember { mutableStateOf(false) }
            val context = LocalContext.current

            if(isAddActivityDialogShowed)
                AddActivityDialog(
                    onDismiss = {isAddActivityDialogShowed = false },
                    category = category
                )
            Column( modifier = Modifier.background(com.example.onehourapp.theme.ui.CardCategoryColor), content = {
                activities.value.forEach { activity ->
                    key(activity.hashCode()) {
                        var dialogMessage by remember {
                            mutableStateOf("")
                        }
                        var showDialog by remember { mutableStateOf(false) }
                        val state = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    showDialog = true
                                }
                                showDialog == false
                            }
                        )
                        if (showDialog)
                            DeleteActivityAlertDialog(
                                dialogMessage,
                                onDelete = {
                                    showDialog = false
                                    viewModel.onEvent(ActivitiesScreenEvent.DeleteActivity(activity.id))
                                    if (activities.value.size == 1)
                                        viewModel.onEvent(ActivitiesScreenEvent.DeleteCategory(category.id))
                                }
                            ) {
                                showDialog = false
                            }
                        SwipeToDismiss(
                            state = state,
                            background = {
                                val color = when (state.dismissDirection) {
                                    DismissDirection.EndToStart -> Color.Red
                                    DismissDirection.StartToEnd -> Color.Transparent
                                    null -> Color.Transparent
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
                            directions = setOf(DismissDirection.EndToStart),
                            dismissContent = {
                                Card(
                                    elevation = 4.dp,
                                    backgroundColor = com.example.onehourapp.theme.ui.CardActivityColor,
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(6.dp, 4.dp, 8.dp, 4.dp)
                                            .background(com.example.onehourapp.theme.ui.CardActivityColor)
                                    ) {
                                        ActivityCardContent(category.color, activity)
                                    }
                                }
                            })
                    }
                }
                Card(
                    elevation = 4.dp,
                    backgroundColor = com.example.onehourapp.theme.ui.CardActivityColor,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(com.example.onehourapp.theme.ui.CardActivityColor)
                    ) {
                        TextButton(
                            onClick = {
                                isAddActivityDialogShowed = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.add),
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .weight(1f),
                                style = com.example.onehourapp.theme.ui.CategoryListItemFont.copy(fontWeight = FontWeight.ExtraBold)
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
    Row(modifier = Modifier.background(com.example.onehourapp.theme.ui.CardActivityColor)) {
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
            style = com.example.onehourapp.theme.ui.ActivityListItemFont
        )
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
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
            ) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = com.example.onehourapp.theme.ui.MainColorSecondRed)
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        modifier = Modifier.shadow(25.dp, ambientColor = Color.White, spotColor = Color.Gray)
    )
}
