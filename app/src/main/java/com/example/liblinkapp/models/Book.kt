package com.example.liblinkapp.models

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val category: String,
    val description: String?,
    val available: Boolean,
    val coverUrl: String?
)