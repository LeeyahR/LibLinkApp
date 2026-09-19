package com.example.liblinkapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.api.ApiService
import com.example.liblinkapp.models.Book
import kotlinx.coroutines.launch

class BrowseActivity : AppCompatActivity() {

    private lateinit var searchBooks: EditText
    private lateinit var bookList: LinearLayout

    private lateinit var api: ApiService

    private var allBooks: List<Book> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_browse)

        searchBooks = findViewById(R.id.searchBooks)
        bookList = findViewById(R.id.bookList)

        api = ApiClient.create(this)

        setupNavigation()

        loadBooks()

        val homeSearch =
            intent.getStringExtra("searchQuery")

        val selectedCategory =
            intent.getStringExtra("category")

        if (!homeSearch.isNullOrEmpty()) {

            searchBooks.setText(homeSearch)

        } else if (!selectedCategory.isNullOrEmpty()) {

            searchBooks.setText(selectedCategory)
        }

        searchBooks.setOnEditorActionListener { _, _, _ ->

            filterBooks(
                searchBooks.text.toString()
            )

            false
        }

        searchBooks.addTextChangedListener(
            object : android.text.TextWatcher {

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
                    before: Int,
                    count: Int
                ) {

                    filterBooks(
                        s.toString()
                    )
                }

                override fun afterTextChanged(
                    s: android.text.Editable?
                ) {
                }
            }
        )
    }

    private fun loadBooks() {

        lifecycleScope.launch {

            try {

                val response =
                    api.getBooks()

                if (response.isSuccessful) {

                    allBooks =
                        response.body()
                            ?: emptyList()

                    filterBooks(
                        searchBooks.text.toString()
                    )

                } else {

                    Toast.makeText(
                        this@BrowseActivity,
                        "Could not load books. " +
                                "Server returned ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@BrowseActivity,
                    "Could not connect to API: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun filterBooks(
        query: String
    ) {

        val search =
            query.trim().lowercase()

        val filteredBooks =
            if (search.isEmpty()) {

                allBooks

            } else {

                allBooks.filter { book ->

                    book.title
                        .lowercase()
                        .contains(search) ||

                            book.author
                                .lowercase()
                                .contains(search) ||

                            book.category
                                .lowercase()
                                .contains(search) ||

                            book.isbn
                                .lowercase()
                                .contains(search)
                }
            }

        displayBooks(filteredBooks)
    }

    private fun displayBooks(
        books: List<Book>
    ) {

        bookList.removeAllViews()

        if (books.isEmpty()) {

            val noBooks =
                TextView(this)

            noBooks.text =
                "No books found."

            noBooks.textSize = 18f
            noBooks.setTextColor(
                Color.rgb(22, 37, 68)
            )

            noBooks.gravity =
                Gravity.CENTER

            noBooks.setPadding(
                20,
                50,
                20,
                50
            )

            bookList.addView(
                noBooks
            )

            return
        }

        for (book in books) {

            val card =
                createBookCard(book)

            bookList.addView(card)
        }
    }

    private fun createBookCard(
        book: Book
    ): LinearLayout {

        val card =
            LinearLayout(this)

        val cardParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(120)
            )

        cardParams.bottomMargin =
            dpToPx(12)

        card.layoutParams =
            cardParams

        card.orientation =
            LinearLayout.HORIZONTAL

        card.gravity =
            Gravity.CENTER_VERTICAL

        card.setPadding(
            dpToPx(12),
            dpToPx(12),
            dpToPx(12),
            dpToPx(12)
        )

        card.setBackgroundResource(
            R.drawable.rounded_white
        )

        // Book cover
        val cover =
            TextView(this)

        val coverParams =
            LinearLayout.LayoutParams(
                dpToPx(75),
                dpToPx(95)
            )

        cover.layoutParams =
            coverParams

        cover.gravity =
            Gravity.CENTER

        cover.setTextColor(
            Color.WHITE
        )

        cover.setTextSize(
            10f
        )

        cover.setTypeface(
            null,
            Typeface.BOLD
        )

        cover.text =
            createCoverText(book.title)

        cover.setBackgroundColor(
            getCoverColor(book.category)
        )

        // Information section
        val information =
            LinearLayout(this)

        val infoParams =
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )

        information.layoutParams =
            infoParams

        information.orientation =
            LinearLayout.VERTICAL

        information.setPadding(
            dpToPx(15),
            0,
            0,
            0
        )

        // Title
        val title =
            TextView(this)

        title.text =
            book.title

        title.setTextColor(
            Color.rgb(22, 37, 68)
        )

        title.textSize =
            17f

        title.setTypeface(
            null,
            Typeface.BOLD
        )

        // Author
        val author =
            TextView(this)

        author.text =
            book.author

        author.setTextColor(
            Color.rgb(104, 113, 135)
        )

        author.textSize =
            12f

        // Category
        val category =
            TextView(this)

        category.text =
            book.category

        category.setTextColor(
            Color.rgb(214, 169, 40)
        )

        category.textSize =
            12f

        // Availability
        val availability =
            TextView(this)

        if (book.available) {

            availability.text =
                "${book.availableCopies} Available"

            availability.setTextColor(
                Color.rgb(60, 128, 91)
            )

        } else {

            availability.text =
                "Unavailable"

            availability.setTextColor(
                Color.rgb(180, 60, 60)
            )
        }

        availability.textSize =
            12f

        availability.setPadding(
            0,
            dpToPx(6),
            0,
            0
        )

        information.addView(title)
        information.addView(author)
        information.addView(category)
        information.addView(availability)

        card.addView(cover)
        card.addView(information)

        card.setOnClickListener {

            openBook(book)
        }

        return card
    }

    private fun openBook(
        book: Book
    ) {

        val intent =
            Intent(
                this,
                BookDetailsActivity::class.java
            )

        intent.putExtra(
            "bookId",
            book.id
        )

        intent.putExtra(
            "bookTitle",
            book.title
        )

        intent.putExtra(
            "author",
            book.author
        )

        intent.putExtra(
            "category",
            book.category
        )

        intent.putExtra(
            "isbn",
            book.isbn
        )

        intent.putExtra(
            "description",
            book.description
        )

        intent.putExtra(
            "coverUrl",
            book.coverUrl
        )

        intent.putExtra(
            "availableCopies",
            book.availableCopies
        )

        startActivity(intent)
    }

    private fun createCoverText(
        title: String
    ): String {

        val words =
            title.split(" ")

        return if (words.size >= 2) {

            words.take(2)
                .joinToString("\n")
                .uppercase()

        } else {

            title.uppercase()
        }
    }

    private fun getCoverColor(
        category: String
    ): Int {

        return when (
            category.lowercase()
        ) {

            "computer science" ->
                Color.rgb(36, 67, 106)

            "mathematics" ->
                Color.rgb(23, 77, 64)

            "law" ->
                Color.rgb(100, 29, 29)

            "business" ->
                Color.rgb(82, 59, 105)

            else ->
                Color.rgb(36, 67, 106)
        }
    }

    private fun dpToPx(
        dp: Int
    ): Int {

        return (
                dp *
                        resources.displayMetrics.density
                ).toInt()
    }

    private fun setupNavigation() {

        findViewById<TextView>(
            R.id.navHome
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()
        }

        findViewById<TextView>(
            R.id.navBrowse
        ).setOnClickListener {
            // Already here
        }

        findViewById<TextView>(
            R.id.navMyBooks
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MyBooksActivity::class.java
                )
            )

            finish()
        }

        findViewById<TextView>(
            R.id.navProfile
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )

            finish()
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