package com.example.liblinkapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.models.AddBookRequest
import kotlinx.coroutines.launch

class AddBookActivity : AppCompatActivity() {

    private lateinit var edtBookTitle : EditText
    private lateinit var edtAuthor : EditText
    private lateinit var edtISBN : EditText
    private lateinit var edtGenre : EditText
    private lateinit var edtDescription : EditText
    private lateinit var edtCoverImage : EditText
    private lateinit var edtTotalCopies : EditText
    private lateinit var edtAvailableCopies : EditText
    private lateinit var btnAddBook : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_book)

        edtBookTitle = findViewById(R.id.edtBookTitle)
        edtAuthor = findViewById(R.id.edtAuthor)
        edtISBN = findViewById(R.id.edtISBN)
        edtGenre = findViewById(R.id.edtGenre)
        edtDescription = findViewById(R.id.edtDescription)
        edtCoverImage = findViewById(R.id.edtCoverImage)
        edtTotalCopies = findViewById(R.id.edtTotalCopies)
        edtAvailableCopies = findViewById(R.id.edtAvailableCopies)
        btnAddBook = findViewById(R.id.btnAddBook)

        btnAddBook.setOnClickListener {
            addBook()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addBook() {

        val title = edtBookTitle.text.toString().trim()
        val author = edtAuthor.text.toString().trim()
        val isbn = edtISBN.text.toString().trim()
        val genre = edtGenre.text.toString().trim()
        val description = edtDescription.text.toString().trim()
        val coverImage = edtCoverImage.text.toString().trim()

        val totalCopiesText =
            edtTotalCopies.text.toString().trim()

        val availableCopiesText =
            edtAvailableCopies.text.toString().trim()

        // Validation

        if (title.isEmpty()) {
            edtBookTitle.error = "Enter the book title"
            edtBookTitle.requestFocus()
            return
        }

        if (author.isEmpty()) {
            edtAuthor.error = "Enter the author"
            edtAuthor.requestFocus()
            return
        }

        if (isbn.isEmpty()) {
            edtISBN.error = "Enter the ISBN"
            edtISBN.requestFocus()
            return
        }

        if (genre.isEmpty()) {
            edtGenre.error = "Enter the genre"
            edtGenre.requestFocus()
            return
        }

        if (totalCopiesText.isEmpty()) {
            edtTotalCopies.error = "Enter total copies"
            edtTotalCopies.requestFocus()
            return
        }

        if (availableCopiesText.isEmpty()) {
            edtAvailableCopies.error = "Enter available copies"
            edtAvailableCopies.requestFocus()
            return
        }

        val totalCopies = totalCopiesText.toIntOrNull()

        val availableCopies =
            availableCopiesText.toIntOrNull()

        if (totalCopies == null || totalCopies < 1) {
            edtTotalCopies.error = "Enter a valid number"
            edtTotalCopies.requestFocus()
            return
        }

        if (availableCopies == null || availableCopies < 0) {
            edtAvailableCopies.error = "Enter a valid number"
            edtAvailableCopies.requestFocus()
            return
        }

        if (availableCopies > totalCopies) {
            edtAvailableCopies.error =
                "Available copies cannot exceed total copies"
            edtAvailableCopies.requestFocus()
            return
        }

        val request = AddBookRequest(
            title = title,
            author = author,
            isbn = isbn,
            genre = genre,
            description = description,
            coverImage = if (coverImage.isEmpty()) {
                null
            } else {
                coverImage
            },
            totalCopies = totalCopies,
            availableCopies = availableCopies
        )

        btnAddBook.isEnabled = false

        lifecycleScope.launch {

            try {

                val response =
                    ApiClient.create(this@AddBookActivity)
                        .addBook(request)

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@AddBookActivity,
                        "Book added successfully",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    val errorMessage =
                        response.errorBody()?.string()

                    Toast.makeText(
                        this@AddBookActivity,
                        "Failed to add book: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()

                    btnAddBook.isEnabled = true
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@AddBookActivity,
                    "Connection error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

                btnAddBook.isEnabled = true
            }
        }
    }
}

