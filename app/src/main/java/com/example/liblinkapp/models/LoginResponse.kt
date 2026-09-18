package com.example.liblinkapp.models

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val token: String?,
    val user: User?
)