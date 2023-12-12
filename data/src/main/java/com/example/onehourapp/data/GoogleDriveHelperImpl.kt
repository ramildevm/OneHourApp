package com.example.onehourapp.data

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.onehourapp.common.GoogleResponseResult
import com.example.onehourapp.data.database.dao.ActivityDao
import com.example.onehourapp.data.database.dao.ActivityRecordDao
import com.example.onehourapp.data.database.dao.CategoryDao
import com.example.onehourapp.data.database.helpers.GoogleDriveHelper
import com.example.onehourapp.data.database.models.dto.DatabaseJsonEntity
import com.example.onehourapp.theme.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.UnknownHostException
import java.util.Collections
import javax.inject.Inject
import javax.inject.Named


class GoogleDriveHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("category") private val categoryDao: CategoryDao,
    @Named("activity")private val activityDao: ActivityDao,
    @Named("activityRecord")private val activityRecordDao: ActivityRecordDao
) :GoogleDriveHelper {
    override suspend fun syncWithDrive(): GoogleResponseResult {
        val accountId = GoogleSignIn.getLastSignedInAccount(context)
        accountId?.let {
            val mDriveService = getGoogleDrive(context, accountId)
            val additionalFolderId = "OneHour_backups_folder_$accountId"
            val additionalFileId = "OneHour_backups_file_$accountId"
            val file = checkSaved(mDriveService, additionalFileId)
            file?.let {
                saveDataToDb(mDriveService, file)
            }
            try {
                val categories = categoryDao.getCategories().first()
                val activities = activityDao.getActivities().first()
                val records = activityRecordDao.getActivityRecords().first()

                val gson = Gson()
                val json = gson.toJson(DatabaseJsonEntity(categories, activities, records))
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val filePath = "onehour_database_backup.json"
                val dbFile = java.io.File(dir, filePath)
                dbFile.writeText(json)

                val gFolder = File()
                gFolder.name = context.resources.getString(R.string.app_name) + " backups"
                gFolder.mimeType = "application/vnd.google-apps.folder"
                gFolder.appProperties = mapOf("additionalID" to additionalFolderId)

                val folderId =
                    if (file == null) mDriveService.Files().create(gFolder).setFields("id")
                        .execute().id else checkSaved(mDriveService, additionalFolderId)!!.id

                val gFile = File()
                gFile.name = dbFile.name
                gFile.appProperties = mapOf("additionalID" to additionalFileId)
                gFile.parents = Collections.singletonList(folderId)

                val fileContent = FileContent("application/json", dbFile)

                checkSaved(mDriveService, additionalFileId)?.let { mDriveService.Files().delete(it.id).execute() }
                mDriveService.Files().create(gFile, fileContent).execute()

                java.io.File(dbFile.path).delete()
                return GoogleResponseResult.SUCCESS
            } catch (exception: IOException) {
                exception.printStackTrace()
                Log.e("Error occurred. ", exception.message!!)
                return GoogleResponseResult.ERROR
            }
        }
        return GoogleResponseResult.ERROR
    }

    private fun checkSaved(mDriveService:Drive, additionalId: String): File? {
        val files: MutableList<File> = ArrayList()
        var pageToken: String? = null
        do {
            try {
                val result: FileList = mDriveService.files().list()
                    .setQ("appProperties has { key='additionalID' and value='$additionalId' }")
                    .setPageToken(pageToken)
                    .execute()
                files.addAll(result.files)
                println(files.size)
                pageToken = result.nextPageToken
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                return null
            }
        } while (pageToken != null)
        return files.lastOrNull()
    }

    private fun getGoogleDrive(context: Context, account: GoogleSignInAccount): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            Collections.singleton(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account

        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            JacksonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName(context.resources.getString(R.string.app_name))
            .build()
    }

    private suspend fun saveDataToDb(mDriveService:Drive, file: File) {
        val gson = Gson()
        val outputStream: OutputStream = ByteArrayOutputStream()
        mDriveService.files().get(file.id).executeMediaAndDownloadTo(outputStream)

        try {
            val data = (outputStream as ByteArrayOutputStream).toByteArray()
            val json = String(data)
            println(json)

            val dataMap = gson.fromJson(json, DatabaseJsonEntity::class.java)

            if (dataMap != null) {
                for (category in dataMap.categories) {
                    categoryDao.insertCategory(category)
                }
                for (activity in dataMap.activities) {
                    activityDao.insertActivity(activity)
                }
                for (record in dataMap.records) {
                    activityRecordDao.insertActivityRecord(record)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}