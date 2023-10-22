package com.example.onehourapp.helpers

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import java.util.concurrent.Executors

class GoogleDriveHelper (private val mDriveService: Drive) {
    private val mExecutor = Executors.newSingleThreadExecutor()
    fun syncData(){

    }
}