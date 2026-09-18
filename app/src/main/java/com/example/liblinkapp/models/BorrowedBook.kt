package com.example.liblinkapp.models

import com.google.gson.annotations.SerializedName

data class BorrowedBook(

    @SerializedName("borrowingID")
    val id: Int,

    @SerializedName("userID")
    val userId: Int,

    @SerializedName("bookID")
    val bookId: Int,

    val borrowedDate: String?,

    val dueDate: String?,

    val returnedDate: String?,

    val status: String,

    val book: Book?
)