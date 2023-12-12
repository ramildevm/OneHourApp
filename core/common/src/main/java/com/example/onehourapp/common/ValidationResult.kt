package com.example.onehourapp.common

data class ValidationResult(
    val successful:Boolean,
    val errorMessage:String?=null
)