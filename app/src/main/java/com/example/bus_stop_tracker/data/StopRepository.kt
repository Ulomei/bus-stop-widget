package com.example.bus_stop_tracker.data

import android.content.Context

data class Stop(
    val id: String,
    val code: String?,
    val name: String,
    val desc: String?,
    val lat: Double?,
    val lon: Double?
)

fun loadStopsFromAssets(context: Context): List<Stop> {
    val stops = mutableListOf<Stop>()
    context.assets.open("stops.txt").bufferedReader().useLines { lines ->
        lines.drop(1).forEach { line -> // skip header row
            val parts = line.split(",")
            if (parts.size >= 6) {
                stops.add(
                    Stop(
                        id = parts[0],
                        code = parts[1].ifBlank { null },
                        name = parts[2].trim('"'),
                        desc = parts[3].trim('"').ifBlank { null },
                        lat = parts[4].toDoubleOrNull(),
                        lon = parts[5].toDoubleOrNull()
                    )
                )
            }
        }
    }
    return stops
}