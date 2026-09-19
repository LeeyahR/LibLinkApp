package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.liblinkapp.utils.SessionManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var btnSaveProfile: Button
    private lateinit var btnLogout: Button

    private lateinit var navHome: TextView
    private lateinit var navBrowse: TextView
    private lateinit var navMyBooks: TextView
    private lateinit var navProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)

        btnSaveProfile = findViewById(R.id.btnSaveProfile)
        btnLogout = findViewById(R.id.btnLogout)

        navHome = findViewById(R.id.navHome)
        navBrowse = findViewById(R.id.navBrowse)
        navMyBooks = findViewById(R.id.navMyBooks)
        navProfile = findViewById(R.id.navProfile)

        val sessionManager = SessionManager(this)

        // Save profile
        btnSaveProfile.setOnClickListener {

            Toast.makeText(
                this,
                "Profile updated successfully",
                Toast.LENGTH_LONG
            ).show()
        }

        // Logout
        btnLogout.setOnClickListener {

            sessionManager.logout()

            val intent =
                Intent(this, MainActivity::class.java)

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()
        }

        // Bottom navigation
        navHome.setOnClickListener {

            startActivity(
                Intent(this, HomeActivity::class.java)
            )

            finish()
        }

        navBrowse.setOnClickListener {

            startActivity(
                Intent(this, BrowseActivity::class.java)
            )

            finish()
        }

        navMyBooks.setOnClickListener {

            startActivity(
                Intent(this, MyBooksActivity::class.java)
            )

            finish()
        }

        navProfile.setOnClickListener {
            // Already on Profile
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