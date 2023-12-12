package com.example.onehourapp.glue.category.di

import com.example.onehourapp.domain.repositories.ActivityRepository
import com.example.onehourapp.domain.repositories.CategoryRepository
import com.example.onehourapp.glue.activity.AdapterActivityRepository
import com.example.onehourapp.glue.category.AdapterCategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CategoryModule {
    @Binds
    fun bindCategoryRepository(categoryRepository: AdapterCategoryRepository): CategoryRepository
}