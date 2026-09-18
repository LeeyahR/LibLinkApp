package com.example.liblinkapp.models

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val studentNumber: String,
    val password: String
)