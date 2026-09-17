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

        //search passed from home page
        val homeSearch = intent.getStringExtra("searchQuery")

        //Category passed from Home page
        val selectedCategory = intent.getStringExtra("category")

        if (!homeSearch.isNullOrEmpty()){
            searchBooks.setText(homeSearch)
        }

        if (!selectedCategory.isNullOrEmpty()){
            searchBooks.setText(selectedCategory)
        }

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
            ) {
                filterBooks(s.toString())
            }

            override fun afterTextChanged(s: Editable?){

            }
        })

        //apply initial search
        filterBooks(searchBooks.text.toString())

        //Book selection

        bookDatabase.setOnClickListener {

            openBook(
                "Database Systems",
                "Korth",
                "Computer Science"
            )
        }

        bookEngineering.setOnClickListener {

            openBook(
                "Engineering Mathematics",
                "K.A Stroud",
                "Mathematics"
            )
        }

        bookLaw.setOnClickListener {

            openBook(
                "Business Law",
                "Linda Edwards",
                "Law"
            )
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

    private fun filterBooks(query: String){

        val search = query.trim().lowercase()

        bookDatabase.visibility = if (search.isEmpty() ||
            "database systems".contains(search) ||
            "computer science".contains(search) ||
            "korth".contains(search)
            ){
            View.VISIBLE
        }else{
            View.GONE
        }

        bookEngineering.visibility = if (search.isEmpty() ||
            "engineering mathematics".contains(search) ||
            "mathematics".contains(search) ||
            "k.a stroud".contains(search)
        ){
            View.VISIBLE
        }else{
            View.GONE
        }

        bookLaw.visibility = if (search.isEmpty() ||
            "business law".contains(search) ||
            "law".contains(search) ||
            "linda edwards".contains(search)
        ){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    private fun openBook(title: String, author: String, category: String){
        val intent = Intent(this, BookDetailsActivity::class.java)

        intent.putExtra("bookTitle", title)
        intent.putExtra("author", author)
        intent.putExtra("category", category)
    }

}