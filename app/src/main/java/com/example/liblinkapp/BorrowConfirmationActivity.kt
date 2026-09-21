package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.api.ApiService
import com.example.liblinkapp.utils.SessionManager
import kotlinx.coroutines.launch

class BorrowConfirmationActivity : AppCompatActivity() {

    private lateinit var btnHome: Button
    private lateinit var btnMyBooks: Button

    private lateinit var api: ApiService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(
            R.layout.activity_borrow_confirmation
        )

        btnHome =
            findViewById(R.id.btnHome)

        btnMyBooks =
            findViewById(R.id.btnMyBooks)

        api =
            ApiClient.create(this)

        sessionManager =
            SessionManager(this)

        val bookId =
            intent.getIntExtra(
                "bookId",
                -1
            )

        val bookTitle =
            intent.getStringExtra(
                "bookTitle"
            ) ?: "Book"

        val action =
            intent.getStringExtra(
                "action"
            ) ?: "borrow"

        val title =
            findViewById<TextView>(
                R.id.confirmationTitle
            )

        val message =
            findViewById<TextView>(
                R.id.confirmationMessage
            )

        if (!sessionManager.isLoggedIn()) {

            goToLogin()
            return
        }

        val userId = sessionManager.getUserId()

        if (userId == -1) {

            showError(
                "No logged-in user was found. Please log out and log in again."
            )

            return
        }

        if (bookId == -1) {

            Toast.makeText(
                this,
                "Invalid book.",
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        if (action == "reserve") {

            title.text = "Book Reservation"
            message.text = "Reserving $bookTitle..."

            reserveBook(
                userId,
                bookId,
                bookTitle
            )

        } else {

            title.text = "Book Borrowing"
            message.text = "Borrowing $bookTitle..."

            borrowBook(
                userId,
                bookId,
                bookTitle
            )
        }


        btnMyBooks.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MyBooksActivity::class.java
                )
            )

            finish()
        }

        btnHome.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }

    private fun borrowBook(
        userId: Int,
        bookId: Int,
        bookTitle: String
    ) {

        val userId =
            sessionManager.getUserId()

        lifecycleScope.launch {

            try {

                val response =
                    api.borrowBook(
                        userId = userId,
                        bookId = bookId
                    )

                if (response.isSuccessful) {

                    findViewById<TextView>(
                        R.id.confirmationTitle
                    ).text =
                        "Book Borrowed"

                    findViewById<TextView>(
                        R.id.confirmationMessage
                    ).text =
                        "$bookTitle has been added to your borrowed books."

                } else {

                    val error =
                        response.errorBody()
                            ?.string()
                            ?.trim()

                    showError(
                        "Borrow failed.\n\nHTTP ${response.code()}\n\nAPI response:\n${error ?: "EMPTY RESPONSE BODY"}"
                    )
                }

            } catch (e: Exception) {

                showError(
                    "Could not connect to API: ${e.message}"
                )
            }
        }
    }

    private fun reserveBook(
        userId: Int,
        bookId: Int,
        bookTitle: String
    ) {

        val userId =
            sessionManager.getUserId()

        lifecycleScope.launch {

            try {

                val response =
                    api.reserveBook(
                        userId = userId,
                        bookId = bookId
                    )

                if (response.isSuccessful) {

                    findViewById<TextView>(
                        R.id.confirmationTitle
                    ).text =
                        "Book Reserved"

                    findViewById<TextView>(
                        R.id.confirmationMessage
                    ).text =
                        "$bookTitle has been added to your reservations."

                } else {

                    val error =
                        response.errorBody()
                            ?.string()
                            ?.trim()

                    showError(
                        "Reservation failed.\n\nHTTP ${response.code()}\n\nAPI response:\n${error ?: "EMPTY RESPONSE BODY"}"
                    )
                }

            } catch (e: Exception) {

                showError(
                    "Could not connect to API: ${e.message}"
                )
            }
        }
    }

    private fun showError(
        message: String
    ) {

        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()

        findViewById<TextView>(
            R.id.confirmationTitle
        ).text =
            "Request Failed"

        findViewById<TextView>(
            R.id.confirmationMessage
        ).text =
            message
    }

    private fun goToLogin() {

        val intent =
            Intent(
                this,
                MainActivity::class.java
            )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()
    }
}