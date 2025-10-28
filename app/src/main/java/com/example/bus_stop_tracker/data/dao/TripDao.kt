package com.example.bus_stop_tracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bus_stop_tracker.data.model.Trip

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trips: List<Trip>)

    @Query("SELECT * FROM trips")
    suspend fun getAllTrips(): List<Trip>
}
