package com.example.onehourapp.ui.screens

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.example.onehourapp.R


class AddRecordDialog(private val context: Context) {

    private val items = arrayOf("Элемент 1", "Элемент 2", "Элемент 3")
    private var selectedItemIndex = 0

    fun show() {
        Log.e("Tag", "recieved проверено2")
//        val alertDialog = AlertDialog.Builder(context)
//            .setTitle("Выберите элемент")
//             .setSingleChoiceItems(items, selectedItemIndex) { _, index ->
//                selectedItemIndex = index
//            }
//            .setPositiveButton("Ок") { _, _ ->
//                // Handle the selected item
//                val selectedItem = items[selectedItemIndex]
//                // Your code to handle the selected item
//            }
//            .setNegativeButton("Отмена") { _, _ -> }
//            .create()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG )
//        else
//            alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG )
//        alertDialog.show()

        Log.e("Tag", "recieved проверено3")
    }
}