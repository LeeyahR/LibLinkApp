package com.example.liblinkapp.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://liblinkapi-dcfgc8d5d6etcrdj.southafricanorth-01.azurewebsites.net/"

    fun create(context: Context): ApiService {

        val client = OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(context)
            )
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}