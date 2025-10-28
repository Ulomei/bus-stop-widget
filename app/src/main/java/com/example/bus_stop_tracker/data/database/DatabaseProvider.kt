package com.example.bus_stop_tracker.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "bus_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
