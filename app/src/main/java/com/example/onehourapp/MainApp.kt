package com.example.onehourapp

import android.app.Application
import android.content.Context
import com.example.onehourapp.data.di.AppModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp :Application()