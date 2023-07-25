package com.example.onehourapp.utils

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
        val value = sharedPref.getString(key, "")
        return value!!
    }
    fun getSharedIntData(context: Context, key: String, default: Int = 0):Int{
        val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val value = sharedPref.getInt(key, default)
        return value!!
    }
    fun getSharedBooleanData(context: Context, key: String):Boolean{
        val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val value = sharedPref.getBoolean(key, false)
        return value!!
    }
}