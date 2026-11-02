package com.example.liblinkapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        BookEntity::class,
        PendingActionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LibLinkDatabase : RoomDatabase() {

    abstract fun libLinkDao(): LibLinkDao

    companion object {

        @Volatile
        private var INSTANCE: LibLinkDatabase? = null

        fun getDatabase(context: Context): LibLinkDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LibLinkDatabase::class.java,
                    "liblink_offline_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}