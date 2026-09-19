package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.api.ApiService
import com.example.liblinkapp.models.BorrowedBook
import com.example.liblinkapp.utils.SessionManager
import kotlinx.coroutines.launch

class MyBooksActivity : AppCompatActivity() {

    private lateinit var borrowedBook: LinearLayout
    private lateinit var borrowedBookTitle: TextView
    private lateinit var dueDate: TextView

    private lateinit var reservedBook: LinearLayout
    private lateinit var reservedBookTitle: TextView

    private lateinit var btnReturn: Button

    private lateinit var navHome: TextView
    private lateinit var navBrowse: TextView
    private lateinit var navMyBooks: TextView
    private lateinit var navProfile: TextView

    private lateinit var api: ApiService
    private lateinit var sessionManager: SessionManager

    private var currentBorrowing: BorrowedBook? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_my_books)

        borrowedBook = findViewById(R.id.borrowedBook)
        borrowedBookTitle = findViewById(R.id.borrowedBookTitle)
        dueDate = findViewById(R.id.dueDate)

        reservedBook = findViewById(R.id.reservedBook)
        reservedBookTitle = findViewById(R.id.reservedBookTitle)

        btnReturn = findViewById(R.id.btnReturn)

        navHome = findViewById(R.id.navHome)
        navBrowse = findViewById(R.id.navBrowse)
        navMyBooks = findViewById(R.id.navMyBooks)
        navProfile = findViewById(R.id.navProfile)

        api = ApiClient.create(this)
        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            goToLogin()
            return
        }

        loadBorrowedBooks()

        btnReturn.setOnClickListener {
            returnBook()
        }

        navHome.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )
            finish()
        }

        navBrowse.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    BrowseActivity::class.java
                )
            )
            finish()
        }

        navMyBooks.setOnClickListener {
            // Already here
        }

        navProfile.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
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

    override fun onResume() {
        super.onResume()

        if (::sessionManager.isInitialized &&
            sessionManager.isLoggedIn()
        ) {
            loadBorrowedBooks()
        }
    }

    private fun loadBorrowedBooks() {

        val userId = sessionManager.getUserId()

        if (userId == -1) {
            goToLogin()
            return
        }

        lifecycleScope.launch {

            try {

                val response =
                    api.getBorrowings(userId)

                if (response.isSuccessful) {

                    val borrowings =
                        response.body() ?: emptyList()

                    /*
                     * Only show active borrowings.
                     */
                    val activeBorrowing =
                        borrowings.firstOrNull {
                            it.returnedDate == null &&
                                    it.status.lowercase() != "returned"
                        }

                    if (activeBorrowing != null) {

                        currentBorrowing =
                            activeBorrowing

                        borrowedBookTitle.text =
                            activeBorrowing.book?.title
                                ?: "Borrowed Book"

                        dueDate.text =
                            if (
                                activeBorrowing.dueDate != null
                            ) {
                                "Due: ${formatDate(activeBorrowing.dueDate)}"
                            } else {
                                "Due date unavailable"
                            }

                        borrowedBook.visibility =
                            View.VISIBLE

                        btnReturn.visibility =
                            View.VISIBLE

                    } else {

                        currentBorrowing = null

                        borrowedBook.visibility =
                            View.GONE

                        btnReturn.visibility =
                            View.GONE
                    }

                } else {

                    borrowedBook.visibility =
                        View.GONE

                    btnReturn.visibility =
                        View.GONE

                    Toast.makeText(
                        this@MyBooksActivity,
                        "Could not load borrowed books.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                borrowedBook.visibility =
                    View.GONE

                btnReturn.visibility =
                    View.GONE

                Toast.makeText(
                    this@MyBooksActivity,
                    "Could not connect to API: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun returnBook() {

        val borrowing =
            currentBorrowing

        if (borrowing == null) {

            Toast.makeText(
                this,
                "No borrowed book found.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        btnReturn.isEnabled = false

        lifecycleScope.launch {

            try {

                val response =
                    api.returnBook(
                        borrowing.id
                    )

                if (response.isSuccessful) {

                    currentBorrowing = null

                    borrowedBook.visibility =
                        View.GONE

                    btnReturn.visibility =
                        View.GONE

                    Toast.makeText(
                        this@MyBooksActivity,
                        "Book returned successfully.",
                        Toast.LENGTH_LONG
                    ).show()

                } else {

                    btnReturn.isEnabled = true

                    Toast.makeText(
                        this@MyBooksActivity,
                        "Could not return the book.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                btnReturn.isEnabled = true

                Toast.makeText(
                    this@MyBooksActivity,
                    "Could not connect to API: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun formatDate(date: String): String {

        return try {

            if (date.contains("T")) {
                date.substringBefore("T")
            } else {
                date
            }

        } catch (e: Exception) {
            date
        }
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