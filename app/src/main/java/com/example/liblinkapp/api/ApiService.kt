package com.example.liblinkapp.api

import com.example.liblinkapp.models.ApiResponse
import com.example.liblinkapp.models.Book
import com.example.liblinkapp.models.BorrowRequest
import com.example.liblinkapp.models.BorrowedBook
import com.example.liblinkapp.models.ForgotPasswordRequest
import com.example.liblinkapp.models.LibraryStats
import com.example.liblinkapp.models.LoginRequest
import com.example.liblinkapp.models.LoginResponse
import com.example.liblinkapp.models.RegisterRequest
import com.example.liblinkapp.models.RegisterResponse
import com.example.liblinkapp.models.Reservation
import com.example.liblinkapp.models.ReservationRequest
import com.example.liblinkapp.models.User

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // -------------------------
    // AUTHENTICATION
    // -------------------------

    @POST("auth/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("auth/register")
    fun register(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>

    @POST("auth/forgot-password")
    fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Call<ApiResponse>


    // -------------------------
    // BOOKS
    // -------------------------

    @GET("books")
    fun getBooks(): Call<List<Book>>

    @GET("books/{id}")
    fun getBook(
        @Path("id") id: Int
    ): Call<Book>

    @GET("books")
    fun searchBooks(
        @Query("search") search: String
    ): Call<List<Book>>

    @GET("books")
    fun getBooksByCategory(
        @Query("category") category: String
    ): Call<List<Book>>


    // -------------------------
    // BORROWING
    // -------------------------

    @POST("borrow")
    fun borrowBook(
        @Body request: BorrowRequest
    ): Call<ApiResponse>

    @GET("users/{userId}/borrowed")
    fun getBorrowedBooks(
        @Path("userId") userId: Int
    ): Call<List<Book>>


    // -------------------------
    // RESERVATIONS
    // -------------------------

    @POST("reservations")
    fun reserveBook(
        @Body request: ReservationRequest
    ): Call<ApiResponse>

    @GET("users/{userId}/reservations")
    fun getReservedBooks(
        @Path("userId") userId: Int
    ): Call<List<Book>>


    // -------------------------
    // USER
    // -------------------------

    @GET("users/{id}")
    fun getUser(
        @Path("id") id: Int
    ): Call<User>

    @GET("users/{userId}/stats")
    fun getLibraryStats(
        @Path("userId") userId: Int
    ): Call<LibraryStats>

    @GET("users/{userId}/history")
    fun getBorrowingHistory(
        @Path("userId") userId: Int
    ): Call<List<BorrowedBook>>

    @GET("users/{userId}/reservations")
    fun getReservations(
        @Path("userId") userId: Int
    ): Call<List<Reservation>>
}