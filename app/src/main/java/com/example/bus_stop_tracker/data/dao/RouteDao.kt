package com.example.bus_stop_tracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bus_stop_tracker.data.model.Route

@Dao
interface RouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routes: List<Route>)

    @Query("SELECT * FROM routes")
    suspend fun getAllRoutes(): List<Route>
}
