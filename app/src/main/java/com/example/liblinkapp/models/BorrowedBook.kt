package com.example.liblinkapp.models

data class BorrowedBook(
    val id: Int,
    val bookId: Int,
    val userId: Int,
    val title: String,
    val author: String,
    val borrowedDate: String?,
    val dueDate: String?,
    val returnedDate: String?,
    val status: String
)