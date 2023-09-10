package com.example.onehourapp.data.database

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.onehourapp.MainApp
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
            val dao = database.get().categoryDao

            applicationScope.launch {
                dao.insertUpdateCategory(Category(1,  applicationContext.getString(R.string.sleep),"#092F79"))
                dao.insertUpdateCategory(Category(2,  applicationContext.getString(R.string.passive),"#4784ED"))
                dao.insertUpdateCategory(Category(3,  applicationContext.getString(R.string.recreation), "#9DD1FF"))
                dao.insertUpdateCategory(Category(4,  applicationContext.getString(R.string.friends), "#FF9BFC"))
                dao.insertUpdateCategory(Category(5,  applicationContext.getString(R.string.family), "#590070"))
                dao.insertUpdateCategory(Category(6,  applicationContext.getString(R.string.reading), "#01A92D"))
                dao.insertUpdateCategory(Category(7,  applicationContext.getString(R.string.exercise), "#ECFF00"))
                dao.insertUpdateCategory(Category(8,  applicationContext.getString(R.string.production), "#FF8F2C"))
                dao.insertUpdateCategory(Category(9,  applicationContext.getString(R.string.work), "#FF0000"))
            }
        }
    }
}