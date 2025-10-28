package com.example.bus_stop_tracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bus_stop_tracker.data.model.Stop

@Dao
interface StopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stops: List<Stop>)

    @Query("SELECT * FROM stops")
    suspend fun getAllStops(): List<Stop>

    @Query("SELECT * FROM stops WHERE stop_name LIKE '%' || :name || '%'")
    suspend fun searchStops(name: String): List<Stop>
}
