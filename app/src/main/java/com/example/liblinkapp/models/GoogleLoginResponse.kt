package com.example.liblinkapp.models

data class GoogleLoginResponse(
    val message: String? = null,
    val requiresRegistration: Boolean = false,
    val userId: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val role: String? = null,
    val token: String? = null
)