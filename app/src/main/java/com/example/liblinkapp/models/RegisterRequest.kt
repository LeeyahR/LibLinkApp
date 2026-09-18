package com.example.liblinkapp.models

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)