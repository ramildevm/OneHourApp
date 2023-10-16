package com.example.onehourapp.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import com.example.onehourapp.data.preferences.SharedPreferencesKeys
import java.util.Locale

object SystemUtil {
    fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        val resources: Resources = activity.resources
        resources.updateConfiguration(configuration, resources.displayMetrics)
        activity.recreate()
    }
    internal fun Context.getActivity(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("Permissions should be called in the context of an Activity")
    }
}