package com.example.liblinkapp.models

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("reviewID")
    val reviewID: Int = 0,

    @SerializedName("userID")
    val userID: Int,

    @SerializedName("bookID")
    val bookID: Int,

    @SerializedName("rating")
    val rating: Int,

    @SerializedName("comment")
    val comment: String,

    @SerializedName("createdDate")
    val createdDate: String? = null,

    @SerializedName("user")
    val user: User? = null
)