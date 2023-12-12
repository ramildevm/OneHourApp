package com.example.onehourapp.common.utils

import android.content.Context

object SharedPreferencesUtil {
    fun setSharedData(context:Context, key:String, value:Any){
        val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        when(value){
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
        }
        editor.apply()
    }
    fun getSharedStringData(context: Context, key: String):String{
        val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPref.getString(key, "")?:""
    }
    fun getSharedIntData(context: Context, key: String, default: Int = 0): Int {
        val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPref.getInt(key, default)
    }
    fun getSharedBooleanData(context: Context, key: String): Boolean {
        val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(key, false)
    }
}