package com.example.onehourapp.glue.category

import android.content.Context
import com.example.onehourapp.common.GoogleResponseResult
import com.example.onehourapp.data.database.helpers.GoogleDriveHelper
import com.example.onehourapp.data.database.repositories.CategoryDataRepository
import com.example.onehourapp.data.database.repositories.UserSettingsDataRepository
import com.example.onehourapp.domain.models.Category
import com.example.onehourapp.domain.repositories.CategoryRepository
import com.example.onehourapp.glue.category.mappers.toEntity
import com.example.onehourapp.glue.category.mappers.toModel
import com.example.onehourapp.notification.NotificationChannelBuilder
import com.example.onehourapp.settings.domain.repositories.GoogleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterCategoryRepository @Inject constructor(
    private val categoryDataRepository: CategoryDataRepository
) : CategoryRepository {

    override fun getCategories(): Flow<List<Category>> {
        return categoryDataRepository.getCategories().map { list->
            list.map {
                it.toModel()
            }
        }
    }

    override suspend fun deleteCategoryById(categoryId: Int):Boolean {
        return categoryDataRepository.deleteCategory(categoryId)
    }

    override suspend fun insertCategory(category: Category) :Int {
        return categoryDataRepository.insertCategory(category.toEntity()).toInt()
    }

}