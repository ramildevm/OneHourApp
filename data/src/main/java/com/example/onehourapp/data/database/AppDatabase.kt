package com.example.onehourapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.onehourapp.theme.R
import com.example.onehourapp.data.database.dao.ActivityDao
import com.example.onehourapp.data.database.dao.ActivityRecordDao
import com.example.onehourapp.data.database.dao.CategoryDao
import com.example.onehourapp.data.database.dao.UserSettingsDao
import com.example.onehourapp.data.database.di.ApplicationScope
import com.example.onehourapp.data.database.models.ActivityEntity
import com.example.onehourapp.data.database.models.ActivityRecordEntity
import com.example.onehourapp.data.database.models.CategoryEntity
import com.example.onehourapp.data.database.models.UserSettingsEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    version = 4,
    entities = [CategoryEntity::class, ActivityEntity::class, ActivityRecordEntity::class, UserSettingsEntity::class],
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
                categoryDao.insertCategory(
                    CategoryEntity(
                        1,
                        applicationContext.getString(R.string.sleep),
                        "#092F79"
                    )
                )
                categoryDao.insertCategory(
                    CategoryEntity(
                        2,
                        applicationContext.getString(R.string.passive),
                        "#4784ED"
                    )
                )
                categoryDao.insertCategory(
                    CategoryEntity(
                        3,
                        applicationContext.getString(R.string.recreation),
                        "#9DD1FF"
                    )
                )
                categoryDao.insertCategory(
                    CategoryEntity(
                        4,
                        applicationContext.getString(R.string.friends),
                        "#FF9BFC"
                    )
                )
                categoryDao.insertCategory(
                    CategoryEntity(
                        5,
                        applicationContext.getString(R.string.family),
                        "#590070"
                    )
                )
                categoryDao.insertCategory(
                    CategoryEntity(
                        6,
                        applicationContext.getString(R.string.reading),
                        "#01A92D"
                    )
                )
                categoryDao.insertCategory(
                    CategoryEntity(
                        7,
                        applicationContext.getString(R.string.exercise),
                        "#ECFF00"
                    )
                )
                categoryDao.insertCategory(
                    CategoryEntity(
                        8,
                        applicationContext.getString(R.string.production),
                        "#FF8F2C"
                    )
                )
                categoryDao.insertCategory(
                    CategoryEntity(
                        9,
                        applicationContext.getString(R.string.work),
                        "#FF0000"
                    )
                )

                activityDao.insertActivity(
                    ActivityEntity(
                        1,
                        applicationContext.getString(R.string.sleep),
                        1
                    )
                )
                activityDao.insertActivity(
                    ActivityEntity(
                        2,
                        applicationContext.getString(R.string.passive),
                        2
                    )
                )
                activityDao.insertActivity(
                    ActivityEntity(
                        3,
                        applicationContext.getString(R.string.recreation),
                        3
                    )
                )
                activityDao.insertActivity(
                    ActivityEntity(
                        4,
                        applicationContext.getString(R.string.friends),
                        4
                    )
                )
                activityDao.insertActivity(
                    ActivityEntity(
                        5,
                        applicationContext.getString(R.string.family),
                        5
                    )
                )
                activityDao.insertActivity(
                    ActivityEntity(
                        6,
                        applicationContext.getString(R.string.reading),
                        6
                    )
                )
                activityDao.insertActivity(
                    ActivityEntity(
                        7,
                        applicationContext.getString(R.string.exercise),
                        7
                    )
                )
                activityDao.insertActivity(
                    ActivityEntity(
                        8,
                        applicationContext.getString(R.string.production),
                        8
                    )
                )
                activityDao.insertActivity(
                    ActivityEntity(
                        9,
                        applicationContext.getString(R.string.work),
                        9
                    )
                )

                settingsDao.insertUpdateUserSettings(
                    UserSettingsEntity(
                        0,
                        1,
                        0L,
                        6,
                        22
                    )
                )
            }
        }
    }
}