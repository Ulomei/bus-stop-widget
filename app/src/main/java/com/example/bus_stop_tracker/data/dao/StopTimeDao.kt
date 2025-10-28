package com.example.bus_stop_tracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bus_stop_tracker.data.model.StopTime

@Dao
interface StopTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stopTimes: List<StopTime>)

    @Query("SELECT * FROM stop_times")
    suspend fun getAllStopTimes(): List<StopTime>
}