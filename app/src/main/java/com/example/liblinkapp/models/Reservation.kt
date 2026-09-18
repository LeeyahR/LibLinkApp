package com.example.liblinkapp.models

import com.google.gson.annotations.SerializedName

data class Reservation(

    @SerializedName("reservationID")
    val id: Int,

    @SerializedName("userID")
    val userId: Int,

    @SerializedName("bookID")
    val bookId: Int,

    val reservationDate: String?,

    val status: String,

    val book: Book?
)