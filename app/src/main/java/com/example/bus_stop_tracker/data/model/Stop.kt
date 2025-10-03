package com.example.bus_stop_tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stops")
data class Stop(
    @PrimaryKey val stop_id: String,
    val stop_name: String,
    val stop_desc: String,
    val stop_lat: Double?,
    val stop_lon: Double?
)