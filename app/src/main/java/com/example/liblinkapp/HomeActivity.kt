package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.api.ApiService
import com.example.liblinkapp.utils.SessionManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    //Global declarations
    private lateinit var navHome : TextView
    private lateinit var navBrowse : TextView
    private lateinit var navMyBooks : TextView
    private lateinit var navProfile : TextView
    private lateinit var txtViewAll : TextView
    private lateinit var txtSearch : EditText
    private lateinit var txtDate : TextView
    private lateinit var categoryComputerScience : TextView
    private lateinit var categoryBusiness : TextView
    private lateinit var categoryMathematics : TextView
    private lateinit var categoryLaw : TextView

    private lateinit var sessionManager : SessionManager

    //api
    private lateinit var api : ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        sessionManager = SessionManager(this)

        //api
        api = ApiClient.create(this)

        navHome = findViewById(R.id.navHome)
        navBrowse = findViewById(R.id.navBrowse)
        navMyBooks = findViewById(R.id.navMyBooks)
        navProfile = findViewById(R.id.navProfile)
        txtViewAll = findViewById(R.id.txtViewAll)
        txtSearch = findViewById(R.id.txtSearch)
        txtDate = findViewById(R.id.txtDate)
        categoryLaw = findViewById(R.id.categoryLaw)
        categoryBusiness = findViewById(R.id.categoryBusiness)
        categoryMathematics = findViewById(R.id.categoryMathematics)
        categoryComputerScience = findViewById(R.id.categoryComputerScience)

        //Current Date
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        txtDate.text = dateFormat.format(Date())

        //Home search
        txtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                val search = txtSearch.text.toString().trim()

                val intent = Intent(this, BrowseActivity::class.java)
                intent.putExtra("searchQuery", search)

                startActivity(intent)

                true
            }else{
                false
            }
        }

        //add button
        findViewById<ImageButton>(R.id.btnAddBook)
            .setOnClickListener {
                startActivity(
                    Intent(this, AddBookActivity::class.java)
                )
            }

        //popular books
        findViewById<android.view.View>(R.id.popularDatabase).setOnClickListener{
            openBook(
                "Database Systems",
                "Korth",
                "Computer Science"
            )
        }

        findViewById<android.view.View>(R.id.popularEngineering).setOnClickListener {
            openBook(
                "Engineering Mathematics",
                "K.A Stroud",
                "Mathematics"
            )
        }

        findViewById<android.view.View>(R.id.popularLaw).setOnClickListener {
            openBook(
                "Business Law",
                "Linda Edwards",
                "Law"
            )
        }

        //open categories
        categoryMathematics.setOnClickListener {
            openCategory("Mathematics")
        }

        categoryComputerScience.setOnClickListener {
            openCategory("Computer Science")
        }

        categoryLaw.setOnClickListener {
            openCategory("Law")
        }

        categoryBusiness.setOnClickListener {
            openCategory("Business")
        }

        //View all books
        txtViewAll.setOnClickListener {
            startActivity(Intent(this, BrowseActivity::class.java))
        }

        //Bottom navigation
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

    override fun onResume(){
        super.onResume()
        updateStatistics()
    }

    private fun updateStatistics(){

        val userId = sessionManager.getUserId()

        if (userId == -1) {
            return
        }

        lifecycleScope.launch {

            try {

                val borrowResponse =
                    api.getBorrowings(userId)

                if (borrowResponse.isSuccessful) {

                    val borrowings =
                        borrowResponse.body() ?: emptyList()

                    val activeBorrowings =
                        borrowings.filter {
                            it.returnedDate == null &&
                                    it.status.lowercase() != "returned"
                        }

                    val history =
                        borrowings.count {
                            it.status.lowercase() == "returned"
                        }

                    findViewById<TextView>(
                        R.id.txtBorrowedCount
                    ).text =
                        activeBorrowings.size.toString()

                    findViewById<TextView>(
                        R.id.txtHistoryCount
                    ).text =
                        history.toString()
                }

            } catch (e: Exception) {

                // Keep the screen usable if the API is unavailable.
                findViewById<TextView>(
                    R.id.txtBorrowedCount
                ).text = "0"

                findViewById<TextView>(
                    R.id.txtHistoryCount
                ).text = "0"
            }
        }

    }

    private fun openCategory(category: String){
        val intent = Intent(this, BrowseActivity::class.java)

        intent.putExtra("category", category)

        startActivity(intent)
    }

    private fun openBook(title: String, author: String, category: String){
        val intent = Intent(this, BrowseActivity::class.java)

        intent.putExtra("bookTitle", title)
        intent.putExtra("author", author)
        intent.putExtra("category", category)

        startActivity(intent)
    }

}