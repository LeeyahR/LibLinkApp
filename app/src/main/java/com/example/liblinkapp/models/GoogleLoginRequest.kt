package com.example.liblinkapp.models

data class GoogleLoginRequest(
    val idToken: String,
    val studentNumber: String? = null
)