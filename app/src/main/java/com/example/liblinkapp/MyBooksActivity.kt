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

class MyBooksActivity : AppCompatActivity() {

    private lateinit var borrowedBook : TextView
    private lateinit var dueDate : TextView
    private lateinit var reservedBook : TextView

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
        borrowedBook = findViewById(R.id.borrowedBook)
        dueDate = findViewById(R.id.dueDate)
        reservedBook = findViewById(R.id.reservedBook)

        val preferences = getSharedPreferences("LibLinkData", MODE_PRIVATE)

        //get saved borrowed book
        val savedBorrowedBook = preferences.getString("borrowedBook", null)

        //get saved reserved book
        val savedReservedBook = preferences.getString("reservedBook", null)

        if (savedBorrowedBook != null ){
            borrowedBook.text = savedBorrowedBook
            dueDate.text = "Due in 14 days"

            btnReturn.visibility = View.VISIBLE
        }else{

            borrowedBook.text = "No Borrowed books"
            dueDate.text = ""
            btnReturn.visibility = View.VISIBLE

        }

        if (savedReservedBook != null){
            reservedBook.text = savedReservedBook
        }else{
            reservedBook.text = "No reserved books"
        }

        btnReturn.setOnClickListener {

            preferences.edit()
                .remove("borrowedBook")
                .apply()

            borrowedBook.text = "No borrowed books"
            dueDate.text = ""
            btnReturn.visibility = View.GONE

            Toast.makeText(this, "Book returned successfully", Toast.LENGTH_LONG).show()
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