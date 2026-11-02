package com.example.liblinkapp.api

import android.content.Context
import com.example.liblinkapp.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    context: Context
) : Interceptor {

    private val sessionManager =
        SessionManager(context.applicationContext)

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val originalRequest = chain.request()

        val token = sessionManager.getToken()

        val requestBuilder =
            originalRequest.newBuilder()

        if (!token.isNullOrBlank()) {
            requestBuilder.header(
                "Authorization",
                "Bearer $token"
            )
        }

        return chain.proceed(
            requestBuilder.build()
        )
    }
}