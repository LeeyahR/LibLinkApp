package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var bookTitle: TextView
    private lateinit var bookAuthor: TextView
    private lateinit var bookCategory: TextView

    private lateinit var btnBack: TextView
    private lateinit var btnBorrow: Button
    private lateinit var btnReserve: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(
            R.layout.activity_book_details
        )

        val bookId =
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
}