package com.example.liblinkapp.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.database.LibLinkDatabase
import com.example.liblinkapp.utils.SessionManager
import java.io.IOException

class PendingActionSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val database = LibLinkDatabase.getDatabase(applicationContext)
        val dao = database.libLinkDao()

        val sessionManager = SessionManager(applicationContext)

        // Do not submit requests if nobody is logged in.
        if (!sessionManager.isLoggedIn()) {
            return Result.retry()
        }

        val currentUserId = sessionManager.getUserId()

        if (currentUserId == -1) {
            return Result.retry()
        }

        val api = ApiClient.create(applicationContext)

        try {
            val pendingActions = dao.getPendingActions()

            // Only sync requests belonging to the currently logged-in user.
            val userActions = pendingActions.filter {
                it.userId == currentUserId
            }

            for (action in userActions) {

                try {
                    val response = when (action.actionType.uppercase()) {

                        "BORROW" -> api.borrowBook(
                            userId = action.userId,
                            bookId = action.bookId
                        )

                        "RESERVE" -> api.reserveBook(
                            userId = action.userId,
                            bookId = action.bookId
                        )

                        else -> {
                            dao.updateActionStatus(
                                requestId = action.requestId,
                                status = "FAILED",
                                error = "Unknown action type."
                            )
                            continue
                        }
                    }

                    when {
                        response.isSuccessful -> {
                            dao.updateActionStatus(
                                requestId = action.requestId,
                                status = "SYNCED",
                                error = null
                            )
                        }

                        response.code() == 401 -> {
                            // The login token may have expired.
                            return Result.retry()
                        }

                        response.code() == 429 || response.code() >= 500 -> {
                            // Temporary server or rate-limit issue.
                            return Result.retry()
                        }

                        else -> {
                            // Permanent API rejection, such as unavailable stock.
                            dao.updateActionStatus(
                                requestId = action.requestId,
                                status = "FAILED",
                                error = "API rejected request: HTTP ${response.code()}"
                            )
                        }
                    }

                } catch (e: IOException) {
                    // Network failure: try again later.
                    return Result.retry()
                }
            }

            return Result.success()

        } catch (e: Exception) {
            return Result.retry()
        }
    }
}