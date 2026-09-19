package com.example.liblinkapp.models

import com.google.gson.annotations.SerializedName

data class BorrowedBook(

    @SerializedName("borrowingID")
    val id: Int,

    @SerializedName("userID")
    val userId: Int,

    @SerializedName("bookID")
    val bookId: Int,

    @SerializedName("borrowedDate")
    val borrowedDate: String?,

    @SerializedName("dueDate")
    val dueDate: String?,

    @SerializedName("returnedDate")
    val returnedDate: String?,

    @SerializedName("status")
    val status: String,

    @SerializedName("book")
    val book: Book?
)