package com.example.onehourapp.utils

import android.content.Context

object SharedReferencesUtil {
    fun setSharedData(context:Context, key:String, value:Any){
        val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        when(value){
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
        }
        editor.apply()
    }
    fun getSharedStringData(context: Context, key: String):String{
        val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val value = sharedPref.getString(key, "")
        return value!!
    }
}