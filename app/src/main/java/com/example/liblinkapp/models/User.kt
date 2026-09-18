package com.example.liblinkapp.models

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("userID")
    val id: Int,

    @SerializedName("fullName")
    val name: String,

    val email: String,

    val studentNumber: String?,

    val role: String?
)