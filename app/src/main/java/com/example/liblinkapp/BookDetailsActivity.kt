package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var bookTitle : TextView
    private lateinit var bookAuthor : TextView
    private lateinit var bookCategory: TextView
    private lateinit var btnBack : TextView
    private lateinit var btnBorrow : Button
    private lateinit var btnReserve : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_details)

        val title = intent.getStringExtra("bookTitle") ?: "Book"
        val author = intent.getStringExtra("author") ?: "Unknown Author"
        val category = intent.getStringExtra("category") ?: "General"

        bookTitle = findViewById(R.id.bookTitle)
        bookAuthor = findViewById(R.id.bookAuthor)
        bookCategory = findViewById(R.id.bookCategory)
        btnBack = findViewById(R.id.btnBack)
        btnBorrow = findViewById(R.id.btnBorrow)
        btnReserve = findViewById(R.id.btnReserve)

        bookTitle.text = title
        bookAuthor.text = author
        bookCategory.text = category

        btnBack.setOnClickListener {
            finish()
        }

        btnBorrow.setOnClickListener {
            val intent = Intent(this, BorrowConfirmationActivity::class.java)

            intent.putExtra("bookTitle", title)
            intent.putExtra("action", "borrow")

            startActivity(intent)
        }

        btnReserve.setOnClickListener {
            val intent = Intent(this, BorrowConfirmationActivity::class.java)

            intent.putExtra("bookTitle", title)
            intent.putExtra("action", "reserve")

            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}