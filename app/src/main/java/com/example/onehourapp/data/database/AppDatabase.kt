package com.example.onehourapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.onehourapp.R
import com.example.onehourapp.data.database.dao.ActivityDao
import com.example.onehourapp.data.database.dao.ActivityRecordDao
import com.example.onehourapp.data.database.dao.CategoryDao
import com.example.onehourapp.data.di.ApplicationScope
import com.example.onehourapp.data.models.Activity
import com.example.onehourapp.data.models.ActivityRecord
import com.example.onehourapp.data.models.Category
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    version = 1,
    entities = [Category::class, Activity::class, ActivityRecord::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val activityDao: ActivityDao
    abstract val activityRecordDao: ActivityRecordDao

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        @ApplicationContext private val applicationContext: Context,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val categoryDao = database.get().categoryDao
            val activityDao = database.get().activityDao

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

                activityDao.insertUpdateActivity(Activity(1,  applicationContext.getString(R.string.sleep),1))
                activityDao.insertUpdateActivity(Activity(2,  applicationContext.getString(R.string.passive),2))
                activityDao.insertUpdateActivity(Activity(3,  applicationContext.getString(R.string.recreation), 3))
                activityDao.insertUpdateActivity(Activity(4,  applicationContext.getString(R.string.friends), 4))
                activityDao.insertUpdateActivity(Activity(5,  applicationContext.getString(R.string.family), 5))
                activityDao.insertUpdateActivity(Activity(6,  applicationContext.getString(R.string.reading), 6))
                activityDao.insertUpdateActivity(Activity(7,  applicationContext.getString(R.string.exercise), 7))
                activityDao.insertUpdateActivity(Activity(8,  applicationContext.getString(R.string.production), 8))
                activityDao.insertUpdateActivity(Activity(9,  applicationContext.getString(R.string.work), 9))
            }
        }
    }
}