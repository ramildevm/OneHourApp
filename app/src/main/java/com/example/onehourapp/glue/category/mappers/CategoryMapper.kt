package com.example.onehourapp.glue.category.mappers

import com.example.onehourapp.data.database.models.ActivityEntity
import com.example.onehourapp.data.database.models.CategoryEntity
import com.example.onehourapp.domain.models.Activity
import com.example.onehourapp.domain.models.Category

fun Category.toEntity():CategoryEntity{
    return CategoryEntity(
        id = this.id,
        name = this.name,
        color = this.color
    )
}
fun CategoryEntity.toModel():Category{
    return Category(
        id = this.id,
        name = this.name,
        color = this.color
    )
}