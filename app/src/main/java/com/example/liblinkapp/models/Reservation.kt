package com.example.liblinkapp.models

data class Reservation(
    val id: Int,
    val bookId: Int,
    val userId: Int,
    val title: String,
    val author: String,
    val reservedDate: String?,
    val status: String
)