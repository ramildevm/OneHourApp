package com.example.onehourapp.glue.activity.mappers

import com.example.onehourapp.data.database.models.ActivityEntity
import com.example.onehourapp.domain.models.Activity

fun Activity.toEntity():ActivityEntity{
    return ActivityEntity(
        id = this.id,
        name = this.name,
        categoryId = this.categoryId
    )
}
fun ActivityEntity.toModel():Activity{
    return Activity(
        id = this.id,
        name = this.name,
        categoryId = this.categoryId
    )
}