package com.example.onehourapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.onehourapp.R
import com.example.onehourapp.data.database.dao.ActivityDao
import com.example.onehourapp.data.database.dao.ActivityRecordDao
import com.example.onehourapp.data.database.dao.CategoryDao
import com.example.onehourapp.data.database.dao.UserSettingsDao
import com.example.onehourapp.data.di.ApplicationScope
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.data.models.Category
import com.example.onehourapp.data.models.UserSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    version = 3,
    entities = [Category::class, Activity::class, ActivityRecord::class, UserSettings::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val activityDao: ActivityDao
    abstract val activityRecordDao: ActivityRecordDao
    abstract val userSettingsDao: UserSettingsDao

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        @ApplicationContext private val applicationContext: Context,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val categoryDao = database.get().categoryDao
            val activityDao = database.get().activityDao
            val settingsDao = database.get().userSettingsDao

            applicationScope.launch {
                categoryDao.insertCategory(Category(1,  applicationContext.getString(R.string.sleep),"#092F79"))
                categoryDao.insertCategory(Category(2,  applicationContext.getString(R.string.passive),"#4784ED"))
                categoryDao.insertCategory(Category(3,  applicationContext.getString(R.string.recreation), "#9DD1FF"))
                categoryDao.insertCategory(Category(4,  applicationContext.getString(R.string.friends), "#FF9BFC"))
                categoryDao.insertCategory(Category(5,  applicationContext.getString(R.string.family), "#590070"))
                categoryDao.insertCategory(Category(6,  applicationContext.getString(R.string.reading), "#01A92D"))
                categoryDao.insertCategory(Category(7,  applicationContext.getString(R.string.exercise), "#ECFF00"))
                categoryDao.insertCategory(Category(8,  applicationContext.getString(R.string.production), "#FF8F2C"))
                categoryDao.insertCategory(Category(9,  applicationContext.getString(R.string.work), "#FF0000"))

                activityDao.insertActivity(Activity(1,  applicationContext.getString(R.string.sleep),1))
                activityDao.insertActivity(Activity(2,  applicationContext.getString(R.string.passive),2))
                activityDao.insertActivity(Activity(3,  applicationContext.getString(R.string.recreation), 3))
                activityDao.insertActivity(Activity(4,  applicationContext.getString(R.string.friends), 4))
                activityDao.insertActivity(Activity(5,  applicationContext.getString(R.string.family), 5))
                activityDao.insertActivity(Activity(6,  applicationContext.getString(R.string.reading), 6))
                activityDao.insertActivity(Activity(7,  applicationContext.getString(R.string.exercise), 7))
                activityDao.insertActivity(Activity(8,  applicationContext.getString(R.string.production), 8))
                activityDao.insertActivity(Activity(9,  applicationContext.getString(R.string.work), 9))

                settingsDao.insertUpdateUserSettings(UserSettings(0,1,0L,0,8, false))
            }
        }
    }
}