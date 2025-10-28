package com.example.bus_stop_tracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bus_stop_tracker.data.dao.*
import com.example.bus_stop_tracker.data.model.*

@Database(
    entities = [Stop::class, Route::class, Trip::class, StopTime::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stopDao(): StopDao
    abstract fun routeDao(): RouteDao
    abstract fun tripDao(): TripDao
    abstract fun stopTimeDao(): StopTimeDao
}
