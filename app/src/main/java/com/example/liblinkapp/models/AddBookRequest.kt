package com.example.liblinkapp.models

data class AddBookRequest(
    val title: String,
    val author: String,
    val isbn: String,
    val genre: String,
    val description: String,
    val coverImage: String?,
    val totalCopies: Int,
    val availableCopies: Int
)