package com.example.bus_stop_tracker.data.repository

import android.content.Context
import com.example.bus_stop_tracker.data.database.AppDatabase
import com.example.bus_stop_tracker.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DataImporter {

    suspend fun importAll(context: Context, db: AppDatabase) {
        withContext(Dispatchers.IO) {
            val assets = context.assets

            val stops = assets.open("stops.txt").bufferedReader().useLines { lines ->
                lines.drop(1).mapNotNull { line ->
                    val parts = line.split(",")
                    if (parts.size >= 6) {
                        Stop(
                            stop_id = parts[0],
                            stop_name = parts[2].trim('"'),
                            stop_desc = parts[3],
                            stop_lat = parts[4].toDoubleOrNull(),
                            stop_lon = parts[5].toDoubleOrNull()
                        )
                    } else null
                }.toList()
            }
            db.stopDao().insertAll(stops)

            val routes = assets.open("routes.txt").bufferedReader().useLines { lines ->
                lines.drop(1).mapNotNull { line ->
                    val parts = line.split(",")
                    if (parts.size >= 9) {
                        Route(
                            route_id = parts[0],
                            route_short_name = parts[2].trim('"'),
                            route_long_name = parts[3].trim('"'),
                            route_color = parts[7]
                        )
                    } else null
                }.toList()
            }
            db.routeDao().insertAll(routes)

            val trips = assets.open("trips.txt").bufferedReader().useLines { lines ->
                lines.drop(1).mapNotNull { line ->
                    val parts = line.split(",")
                    if (parts.size >= 3) {
                        Trip(
                            trip_id = parts[2],
                            route_id = parts[0]
                        )
                    } else null
                }.toList()
            }
            db.tripDao().insertAll(trips)

            val stopTimes = assets.open("stop_times.txt").bufferedReader().useLines { lines ->
                lines.drop(1).mapNotNull { line ->
                    val parts = line.split(",")
                    if (parts.size >= 5) {
                        StopTime(
                            trip_id = parts[0],
                            arrival_time = parts[1],
                            departure_time = parts[2],
                            stop_id = parts[3],
                            stop_sequence = parts[4].toIntOrNull() ?: 0
                        )
                    } else null
                }.toList()
            }
            db.stopTimeDao().insertAll(stopTimes)
        }
    }
}
