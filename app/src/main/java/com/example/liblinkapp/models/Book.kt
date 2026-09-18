package com.example.liblinkapp.models

import com.google.gson.annotations.SerializedName

data class Book(

    @SerializedName("bookID")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("author")
    val author: String,

    @SerializedName("isbn")
    val isbn: String,

    @SerializedName("genre")
    val category: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("coverImage")
    val coverUrl: String?,

    @SerializedName("totalCopies")
    val totalCopies: Int,

    @SerializedName("availableCopies")
    val availableCopies: Int
) {
    val available: Boolean
        get() = availableCopies > 0
}