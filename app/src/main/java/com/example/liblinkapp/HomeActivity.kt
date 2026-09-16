package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {

    //Global declarations
    private lateinit var navHome : TextView
    private lateinit var navBrowse : TextView
    private lateinit var navMyBooks : TextView
    private lateinit var navProfile : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        navHome = findViewById(R.id.navHome)
        navBrowse = findViewById(R.id.navBrowse)
        navMyBooks = findViewById(R.id.navMyBooks)
        navProfile = findViewById(R.id.navProfile)

        navHome.setOnClickListener {
            //Already on Home
        }

        navBrowse.setOnClickListener {
            startActivity(Intent(this, BrowseActivity::class.java))
        }

        navMyBooks.setOnClickListener {
            startActivity(Intent(this, MyBooksActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}