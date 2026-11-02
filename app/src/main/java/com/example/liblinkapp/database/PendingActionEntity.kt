package com.example.liblinkapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "pending_actions")
data class PendingActionEntity(

    @PrimaryKey
    val requestId: String = UUID.randomUUID().toString(),

    val userId: Int,
    val bookId: Int,

    // BORROW or RESERVE
    val actionType: String,

    // PENDING, SYNCED or FAILED
    val status: String = "PENDING",

    val createdAt: Long = System.currentTimeMillis(),

    val errorMessage: String? = null
)