package com.example.liblinkapp.api

import com.example.liblinkapp.models.Book
import com.example.liblinkapp.models.BorrowedBook
import com.example.liblinkapp.models.LoginRequest
import com.example.liblinkapp.models.LoginResponse
import com.example.liblinkapp.models.RegisterRequest
import com.example.liblinkapp.models.RegisterResponse
import com.example.liblinkapp.models.Reservation
import retrofit2.Call
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

    @POST("auth/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("auth/register")
    fun register(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>


    // =========================
    // BOOKS
    // =========================

    @GET("books")
    fun getBooks(): Call<List<Book>>

    @GET("books/{id}")
    fun getBook(
        @Path("id") id: Int
    ): Call<Book>

    @GET("books/search")
    fun searchBooks(
        @Query("search") search: String
    ): Call<List<Book>>


    // =========================
    // BORROWING
    // =========================

    @POST("borrowings")
    fun borrowBook(
        @Query("userId") userId: Int,
        @Query("bookId") bookId: Int
    ): Call<BorrowedBook>

    @PUT("borrowings/{id}/return")
    fun returnBook(
        @Path("id") borrowingId: Int
    ): Call<BorrowedBook>

    @GET("borrowings/user/{userId}")
    fun getUserBorrowings(
        @Path("userId") userId: Int
    ): Call<List<BorrowedBook>>


    // =========================
    // RESERVATIONS
    // =========================

    @POST("reservations")
    fun reserveBook(
        @Query("userId") userId: Int,
        @Query("bookId") bookId: Int
    ): Call<Reservation>

    @GET("reservations/user/{userId}")
    fun getUserReservations(
        @Path("userId") userId: Int
    ): Call<List<Reservation>>
}