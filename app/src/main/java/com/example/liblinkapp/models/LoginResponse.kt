package com.example.liblinkapp.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("message")
    val message: String?,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("role")
    val role: String
)