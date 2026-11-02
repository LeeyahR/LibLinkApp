package com.example.liblinkapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LibLinkDao {

    // BOOK CACHE

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBooks(books: List<BookEntity>)

    @Query("SELECT * FROM cached_books ORDER BY title ASC")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM cached_books WHERE title LIKE '%' || :search || '%' OR author LIKE '%' || :search || '%'")
    suspend fun searchBooks(search: String): List<BookEntity>

    @Query("DELETE FROM cached_books")
    suspend fun clearBooks()


    // OFFLINE ACTIONS

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPendingAction(action: PendingActionEntity)

    @Query("SELECT * FROM pending_actions WHERE status = 'PENDING' ORDER BY createdAt ASC")
    suspend fun getPendingActions(): List<PendingActionEntity>

    @Query("UPDATE pending_actions SET status = :status, errorMessage = :error WHERE requestId = :requestId")
    suspend fun updateActionStatus(
        requestId: String,
        status: String,
        error: String? = null
    )

    @Query("DELETE FROM pending_actions WHERE requestId = :requestId")
    suspend fun deleteAction(requestId: String)

    @Query("SELECT COUNT(*) FROM pending_actions WHERE status = 'PENDING'")
    suspend fun getPendingCount(): Int
}