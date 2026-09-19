package com.example.liblinkapp.api

import com.example.liblinkapp.models.Book
import com.example.liblinkapp.models.BorrowedBook
import com.example.liblinkapp.models.LoginRequest
import com.example.liblinkapp.models.LoginResponse
import com.example.liblinkapp.models.RegisterRequest
import com.example.liblinkapp.models.RegisterResponse
import com.example.liblinkapp.models.Reservation
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // =========================
    // AUTH
    // =========================

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>


    // =========================
    // BOOKS
    // =========================

    @GET("api/books")
    suspend fun getBooks(): Response<List<Book>>

    @GET("api/books/{id}")
    suspend fun getBook(
        @Path("id") id: Int
    ): Response<Book>

    @GET("api/books/search")
    suspend fun searchBooks(
        @Query("search") search: String
    ): Response<List<Book>>


    // =========================
    // BORROWING
    // =========================

    @POST("api/borrowings")
    suspend fun borrowBook(
        @Query("userId") userId: Int,
        @Query("bookId") bookId: Int
    ): Response<BorrowedBook>

    @PUT("api/borrowings/{id}/return")
    suspend fun returnBook(
        @Path("id") id: Int
    ): Response<BorrowedBook>

    @GET("api/borrowings/user/{userId}")
    suspend fun getBorrowings(
        @Path("userId") userId: Int
    ): Response<List<BorrowedBook>>


    // =========================
    // RESERVATIONS
    // =========================

    @POST("api/reservations")
    suspend fun reserveBook(
        @Query("userId") userId: Int,
        @Query("bookId") bookId: Int
    ): Response<Reservation>
}