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

class MyBooksActivity : AppCompatActivity() {

    private lateinit var btnReturn : Button
    private lateinit var navHome : TextView
    private lateinit var navBrowse : TextView
    private lateinit var navMyBooks : TextView
    private lateinit var navProfile : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_books)

        btnReturn = findViewById(R.id.btnReturn)
        navHome = findViewById(R.id.navHome)
        navMyBooks = findViewById(R.id.navMyBooks)
        navProfile = findViewById(R.id.navProfile)
        navBrowse = findViewById(R.id.navBrowse)

        btnReturn.setOnClickListener {
            Toast.makeText(this, "Book returned successfully", Toast.LENGTH_LONG).show()
            btnReturn.isEnabled = false
            btnReturn.text = "Returned"
        }

        //Bottom nav
        navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        navBrowse.setOnClickListener {
            startActivity(Intent(this, BrowseActivity::class.java))
            finish()
        }

        navMyBooks.setOnClickListener {
            //Already here
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}