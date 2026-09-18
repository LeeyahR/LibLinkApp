package com.example.liblinkapp.api

import android.content.Context
import com.example.liblinkapp.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    context: Context
) : Interceptor {

    private val sessionManager =
        SessionManager(context)

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val token =
            sessionManager.getToken()

        val request =
            chain.request()
                .newBuilder()

        if (!token.isNullOrEmpty()) {

            request.addHeader(
                "Authorization",
                "Bearer $token"
            )
        }

        return chain.proceed(
            request.build()
        )
    }
}