package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BrowseActivity : AppCompatActivity() {

    private lateinit var searchBooks : EditText
    private lateinit var bookDatabase : LinearLayout
    private lateinit var bookEngineering : LinearLayout
    private lateinit var bookLaw : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_browse)

        searchBooks = findViewById(R.id.searchBooks)
        bookDatabase = findViewById(R.id.bookDatabase)
        bookEngineering = findViewById(R.id.bookEngineering)
        bookLaw = findViewById(R.id.bookLaw)

        //Search functionality
        searchBooks.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ){
                val search = s.toString().lowercase()

                bookDatabase.visibility =
                    if ("database systems".contains(search) ||
                        "computer science".contains(search)) {
                        View.VISIBLE
                    }else{
                        View.GONE
                    }

                bookEngineering.visibility =
                    if ("engineering mathematics".contains(search) ||
                        "mathematics".contains(search)){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }
                bookLaw.visibility =
                    if ("business law".contains(search) ||
                        "law".contains(search)){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }

            }

            override fun afterTextChanged(s: Editable?){

            }
        })

        //Book selection

        bookDatabase.setOnClickListener {
            val intent = Intent(this, BookDetailsActivity::class.java)

            intent.putExtra("bookTitle", "Database Systems")
            intent.putExtra("category", "Computer Science")
            intent.putExtra("author", "Korth")

            startActivity(intent)
        }

        bookEngineering.setOnClickListener {
            val intent = Intent(this, BookDetailsActivity::class.java)

            intent.putExtra("bookTitle", "Engineering Mathematics")
            intent.putExtra("category", "Mathematics")
            intent.putExtra("author", "K.A Stroud")

            startActivity(intent)
        }

        bookLaw.setOnClickListener {
            val intent = Intent(this, BookDetailsActivity::class.java)

            intent.putExtra("bookTitle", "Business Law")
            intent.putExtra("category", "Law")
            intent.putExtra("author", "Linda Edwards")

            startActivity(intent)
        }

        //Bottom navigation

        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navBrowse).setOnClickListener {
            // Already here
        }

        findViewById<TextView>(R.id.navMyBooks).setOnClickListener {
            startActivity(Intent(this, MyBooksActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navProfile).setOnClickListener {
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