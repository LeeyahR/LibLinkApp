package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BorrowConfirmationActivity : AppCompatActivity() {


    private lateinit var btnHome : Button
    private lateinit var btnMyBooks : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_borrow_confirmation)

        val bookTitle = intent.getStringExtra("bookTitle") ?: "Book"
        val action = intent.getStringExtra("action") ?: "borrow"
        val title = findViewById<TextView>(R.id.confirmationTitle)
        val message = findViewById<TextView>(R.id.confirmationMessage)

        btnHome = findViewById(R.id.btnHome)


        if (action == "reserve"){
            title.text = "Book Reserved"

            message.text = "$bookTitle has been added to your reservations."

        }else{
            title.text = "Book Borrowed"

            message.text = "$bookTitle has been added to your borrowed books."
        }

        btnMyBooks.setOnClickListener{
            startActivity(Intent(this, MyBooksActivity::class.java))
            finish()
        }

        btnHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}