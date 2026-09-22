package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.api.ApiService
import com.example.liblinkapp.models.Review
import com.example.liblinkapp.utils.SessionManager
import kotlinx.coroutines.launch

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var bookTitle: TextView
    private lateinit var bookAuthor: TextView
    private lateinit var bookCategory: TextView

    private lateinit var btnBack: TextView
    private lateinit var btnBorrow: Button
    private lateinit var btnReserve: Button

    private lateinit var ratingBar: RatingBar
    private lateinit var edtReview: EditText
    private lateinit var btnSubmitReview: Button
    private lateinit var txtReviews: TextView

    private lateinit var api : ApiService
    private lateinit var sessionManager : SessionManager

    private var bookId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(
            R.layout.activity_book_details
        )

        api = ApiClient.create(this)
        sessionManager = SessionManager(this)

         bookId =
            intent.getIntExtra(
                "bookId",
                -1
            )

        val title =
            intent.getStringExtra(
                "bookTitle"
            ) ?: "Book"

        val author =
            intent.getStringExtra(
                "author"
            ) ?: "Unknown Author"

        val category =
            intent.getStringExtra(
                "category"
            ) ?: "General"

        val availableCopies =
            intent.getIntExtra(
                "availableCopies",
                0
            )

        bookTitle =
            findViewById(R.id.bookTitle)

        bookAuthor =
            findViewById(R.id.bookAuthor)

        bookCategory =
            findViewById(R.id.bookCategory)

        btnBack =
            findViewById(R.id.btnBack)

        btnBorrow =
            findViewById(R.id.btnBorrow)

        btnReserve =
            findViewById(R.id.btnReserve)

        ratingBar =
            findViewById(R.id.ratingBar)

        edtReview =
            findViewById(R.id.edtReview)

        btnSubmitReview =
            findViewById(R.id.btnSubmitReview)

        txtReviews =
            findViewById(R.id.txtReviews)

        bookTitle.text = title
        bookAuthor.text = author
        bookCategory.text = category

        if (bookId == -1) {

            Toast.makeText(
                this,
                "Invalid book.",
                Toast.LENGTH_LONG
            ).show()

            btnBorrow.isEnabled = false
            btnReserve.isEnabled = false
        }

        if (availableCopies <= 0) {

            btnBorrow.isEnabled = false
            btnBorrow.text = "Unavailable"

            btnReserve.isEnabled = true
            btnReserve.visibility = View.VISIBLE

        } else {

            btnBorrow.isEnabled = true

            btnReserve.isEnabled = false
            btnReserve.text = "Available to Borrow"
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnBorrow.setOnClickListener {

            val intent =
                Intent(
                    this,
                    BorrowConfirmationActivity::class.java
                )

            intent.putExtra(
                "bookId",
                bookId
            )

            intent.putExtra(
                "bookTitle",
                title
            )

            intent.putExtra(
                "action",
                "borrow"
            )

            startActivity(intent)
        }

        btnReserve.setOnClickListener {

            val intent =
                Intent(
                    this,
                    BorrowConfirmationActivity::class.java
                )

            intent.putExtra(
                "bookId",
                bookId
            )

            intent.putExtra(
                "bookTitle",
                title
            )

            intent.putExtra(
                "action",
                "reserve"
            )

            startActivity(intent)
        }

        btnSubmitReview.setOnClickListener {
            Toast.makeText(
                this,
                "Submit button clicked",
                Toast.LENGTH_SHORT
            ).show()

            submitReview()
        }

        loadReviews()


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

    private fun submitReview() {

        Toast.makeText(
            this,
            "Submitting review...",
            Toast.LENGTH_SHORT
        ).show()

        if (bookId == -1) {
            return
        }

        if (!sessionManager.isLoggedIn()) {

            Toast.makeText(
                this,
                "Please log in to submit a review.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        val userId =
            sessionManager.getUserId()

        if (userId == -1) {

            Toast.makeText(
                this,
                "User session not found.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        val rating =
            ratingBar.rating.toInt()

        val comment =
            edtReview.text.toString().trim()

        if (rating < 1) {

            Toast.makeText(
                this,
                "Please select a rating.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        if (comment.isEmpty()) {

            edtReview.error =
                "Please write a review."

            edtReview.requestFocus()

            return
        }

        val review =
            Review(
                userID = userId,
                bookID = bookId,
                rating = rating,
                comment = comment
            )

        btnSubmitReview.isEnabled = false

        lifecycleScope.launch {

            try {

                val response =
                    api.addReview(review)

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@BookDetailsActivity,
                        "Review submitted!",
                        Toast.LENGTH_LONG
                    ).show()

                    ratingBar.rating = 0f
                    edtReview.text.clear()

                    loadReviews()

                } else {

                    val error =
                        response.errorBody()
                            ?.string()

//                    Toast.makeText(
//                        this@BookDetailsActivity,
//                        "Review failed\nHTTP ${response.code()}\n$error",
//                        Toast.LENGTH_LONG
//                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@BookDetailsActivity,
                    "Could not connect to API: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                btnSubmitReview.isEnabled = true
            }
        }
    }

    private fun loadReviews() {

        if (bookId == -1) {
            return
        }

        lifecycleScope.launch {

            try {

                val response =
                    api.getReviews(bookId)

                if (response.isSuccessful) {

                    val reviews =
                        response.body()
                            ?: emptyList()

                    displayReviews(reviews)

                } else {

                    txtReviews.text =
                        "Could not load reviews."
                }

            } catch (e: Exception) {

                txtReviews.text =
                    "Could not load reviews."
            }
        }
    }

    private fun displayReviews(
        reviews: List<Review>
    ) {

        if (reviews.isEmpty()) {

            txtReviews.text =
                "No reviews yet."

            return
        }

        val text =
            StringBuilder()

        for (review in reviews) {

            val name =
                review.user?.name
                    ?: "User"

            text.append(
                "$name\n"
            )

            text.append(
                "Rating: ${"★".repeat(review.rating)}\n"
            )

            text.append(
                "${review.comment}\n\n"
            )
        }

        txtReviews.text =
            text.toString()
    }
}