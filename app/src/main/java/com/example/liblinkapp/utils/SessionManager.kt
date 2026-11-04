package com.example.liblinkapp.utils

import android.content.Context

class SessionManager(context: Context) {

    private val preferences =
        context.getSharedPreferences(
            "LibLinkSession",
            Context.MODE_PRIVATE
        )

    fun saveSession(
        userId: Int,
        name: String,
        email: String,
        role: String,
        token: String? = null
    ) {
        preferences.edit()
            .putInt("userId", userId)
            .putString("name", name)
            .putString("email", email)
            .putString("role", role)
            .putString("token", token)
            .putBoolean("loggedIn", true)
            .apply()
    }

    fun getUserId(): Int =
        preferences.getInt("userId", -1)

    fun getName(): String? =
        preferences.getString("name", null)

    fun getEmail(): String? =
        preferences.getString("email", null)

    fun getRole(): String? =
        preferences.getString("role", null)

    fun getToken(): String? =
        preferences.getString("token", null)

    fun isLoggedIn(): Boolean =
        preferences.getBoolean("loggedIn", false)

    fun isBiometricEnabled(): Boolean =
        preferences.getBoolean("biometricEnabled", false)

    fun setBiometricEnabled(enabled: Boolean) {
        preferences.edit()
            .putBoolean("biometricEnabled", enabled)
            .apply()
    }

    fun logout() {
        preferences.edit()
            .clear()
            .apply()
    }
}