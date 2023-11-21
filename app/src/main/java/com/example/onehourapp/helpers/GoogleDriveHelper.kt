package com.example.onehourapp.helpers

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.onehourapp.R
import com.example.onehourapp.data.models.dto.DatabaseJson
import com.example.onehourapp.ui.viewmodels.ActivityRecordViewModel
import com.example.onehourapp.ui.viewmodels.ActivityViewModel
import com.example.onehourapp.ui.viewmodels.CategoryViewModel
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okio.IOException
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.net.UnknownHostException
import java.util.Collections
import java.util.concurrent.Executors


class GoogleDriveHelper (
    private val mDriveService: Drive) {
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()
    }
    fun syncWithDrive(
        context: Context,
        scope: CoroutineScope,
        accountId: String,
        categoryViewModel: CategoryViewModel,
        activityViewModel: ActivityViewModel,
        activityRecordViewModel: ActivityRecordViewModel,
        onFinish: (result: ResponseResult) -> Unit
    ) {
        scope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val additionalFolderId = "OneHour_backups_folder_$accountId"
            val additionalFileId = "OneHour_backups_file_$accountId"
            val file = checkSaved(additionalFileId)
            file?.let {
                saveDataToDb(file, categoryViewModel, activityViewModel, activityRecordViewModel)
            }
            try {
                val categories = categoryViewModel.getCategories().first()
                val activities = activityViewModel.getActivities().first()
                val records = activityRecordViewModel.getActivityRecords().first()

                val gson = Gson()
                val json = gson.toJson(DatabaseJson(categories, activities, records))
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val filePath = "onehour_database_backup.json"
                val dbFile = java.io.File(dir, filePath)
                dbFile.writeText(json)

                val gFolder = File()
                gFolder.name = context.resources.getString(R.string.app_name) + " backups"
                gFolder.mimeType = "application/vnd.google-apps.folder"
                gFolder.appProperties = mapOf("additionalID" to additionalFolderId)

                val folderId = if(file==null) mDriveService.Files().create(gFolder).setFields("id").execute().id else checkSaved(additionalFolderId)!!.id

                val gFile = File()
                gFile.name = dbFile.name
                gFile.appProperties = mapOf("additionalID" to additionalFileId)
                gFile.parents = Collections.singletonList(folderId)

                val fileContent = FileContent("application/json", dbFile)

                checkSaved(additionalFileId)?.let{mDriveService.Files().delete(it.id).execute()}
                mDriveService.Files().create(gFile, fileContent).execute()

                java.io.File(dbFile.path).delete()
                onFinish(ResponseResult.SUCCESS)
            } catch (exception: IOException) {
                exception.printStackTrace()
                Log.e("Error occurred. ", exception.message!!)
                onFinish(ResponseResult.ERROR)
            }
        }
    }

    private fun checkSaved(additionalId: String): File? {
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
            }
            catch (e: UnknownHostException){
                e.printStackTrace()
                return null
            }
        } while (pageToken != null)
        return files.lastOrNull()
    }

    private fun saveDataToDb(file: File, categoryViewModel: CategoryViewModel, activityViewModel: ActivityViewModel, activityRecordViewModel: ActivityRecordViewModel) {
        val gson = Gson()
        val outputStream: OutputStream = ByteArrayOutputStream()
        mDriveService.files().get(file.id).executeMediaAndDownloadTo(outputStream)

        try {
            val data = (outputStream as ByteArrayOutputStream).toByteArray()
            val json = String(data)
            println(json)

            val dataMap = gson.fromJson(json, DatabaseJson::class.java)

            if (dataMap != null) {
                for (category in dataMap.categories){
                    categoryViewModel.insertCategory(category)
                }
                for (activity in dataMap.activities) {
                    activityViewModel.insertActivity(activity)
                }
                for (record in dataMap.records) {
                    activityRecordViewModel.insertActivityRecord(record)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    enum class ResponseResult{
        DEFAULT,
        SUCCESS,
        ERROR
    }
}