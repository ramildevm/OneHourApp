package com.example.onehourapp.dialog.record.presentation

import com.example.onehourapp.domain.models.Activity

sealed class AddRecordDialogEvent{
    data class SelectedActivityIdChanged(val id:Int) : AddRecordDialogEvent()
    data class OnAdd(
        val date: Long,
        val startHour: Int,
        val endHour: Int,
        val activity:Activity?=null
    ) : AddRecordDialogEvent()
}
