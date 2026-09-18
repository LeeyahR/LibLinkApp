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
        token: String?
    ) {
        preferences.edit()
            .putInt("userId", userId)
            .putString("name", name)
            .putString("email", email)
            .putString("token", token)
            .putBoolean("loggedIn", true)
            .apply()
    }

    fun getUserId(): Int {
        return preferences.getInt("userId", -1)
    }

    fun getName(): String? {
        return preferences.getString("name", null)
    }

    fun getEmail(): String? {
        return preferences.getString("email", null)
    }

    fun getToken(): String? {
        return preferences.getString("token", null)
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean("loggedIn", false)
    }

    fun logout() {
        preferences.edit().clear().apply()
    }
}