package com.example.liblinkapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.liblinkapp.models.Book

@Entity(tableName = "cached_books")
data class BookEntity(

    @PrimaryKey
    val id: Int,

    val title: String,
    val author: String,
    val isbn: String,
    val category: String,
    val description: String?,
    val coverUrl: String?,
    val totalCopies: Int,
    val availableCopies: Int
) {
    fun toBook(): Book {
        return Book(
            id = id,
            title = title,
            author = author,
            isbn = isbn,
            category = category,
            description = description,
            coverUrl = coverUrl,
            totalCopies = totalCopies,
            availableCopies = availableCopies
        )
    }

    companion object {
        fun fromBook(book: Book): BookEntity {
            return BookEntity(
                id = book.id,
                title = book.title,
                author = book.author,
                isbn = book.isbn,
                category = book.category,
                description = book.description,
                coverUrl = book.coverUrl,
                totalCopies = book.totalCopies,
                availableCopies = book.availableCopies
            )
        }
    }
}