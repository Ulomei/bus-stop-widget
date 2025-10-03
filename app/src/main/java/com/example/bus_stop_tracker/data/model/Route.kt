package com.example.bus_stop_tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class Route(
    @PrimaryKey val route_id: String,
    val route_short_name: String,
    val route_long_name: String,
    val route_color: String,
)