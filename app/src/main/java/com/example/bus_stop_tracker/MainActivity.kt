package com.example.bus_stop_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import com.example.bus_stop_tracker.data.model.Stop
import com.example.bus_stop_tracker.ui.theme.BusstoptrackerTheme
import androidx.lifecycle.lifecycleScope
import com.example.bus_stop_tracker.data.database.DatabaseProvider
import com.example.bus_stop_tracker.data.repository.DataImporter
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)
        lifecycleScope.launch {
            DataImporter.importAll(this@MainActivity, db)
        }

        lifecycleScope.launch {
            DataImporter.importAll(this@MainActivity, db)
            val count = db.stopDao().getAllStops().size
            android.util.Log.d("MainActivity", "Loaded $count stops into DB")
        }
        /*val stops = loadStopsFromAssets(this)
        Log.d("MainActivity", "Loaded ${stops.size} stops")
        if (stops.isNotEmpty()) {
            Log.d("MainActivity", "First stop: ${stops.first()}")
        }
        if (stops.size > 1) {
            Log.d("MainActivity", "Second stop: ${stops[4]}")
        }*/

        enableEdgeToEdge()
        setContent {
            BusstoptrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var showSearch by remember { mutableStateOf(false) }
    var selectedStops by remember { mutableStateOf(listOf<Stop>()) }
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }

    val allStops by produceState(initialValue = emptyList<Stop>(), context) {
        value = db.stopDao().getAllStops()
    }

    if (showSearch) {
        SearchScreen(
            allStops = allStops,
            onStopSelected = { stop: Stop ->
                if (!selectedStops.contains(stop)) {
                    selectedStops = selectedStops + stop
                }
                showSearch = false
            },
            onBack = { showSearch = false },
            modifier = modifier
        )
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxSize()        // Take up full screen
                .padding(16.dp)
        ) {
            Text(
                text = "Widget Setup",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            OutlinedButton(
                onClick = { showSearch = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(40.dp)
            )
            {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Search for a stop")
            }

            Text(
                text = "Currently displaying: ",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Column {
                if(selectedStops.isEmpty()) {
                    Text("No selected stops")
                } else {
                    selectedStops.forEach { stop ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    stop.stop_name,
                                )
                                Text(
                                    text = stop.stop_desc ?: "-",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Button (onClick = { selectedStops = selectedStops - stop },
                                modifier = Modifier.size(50.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.Black
                                )
                            ) {Text("x", textAlign = TextAlign.End) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen(allStops: List<Stop>, onStopSelected: (Stop) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {

    var query by remember { mutableStateOf("") }

    val filteredStops = allStops.filter { it.stop_name.contains(query, ignoreCase = true) }
    val visibleStops = filteredStops.take(20)

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .padding( horizontal = 4.dp)
        ){
            Button(onClick = { onBack() },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black)) {
                Text("<")
            }

            //TO DO allow Lithuanian letters
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search for a stop") },
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
        HorizontalDivider()

        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            visibleStops.forEach { stop ->
                Button(
                    onClick = { onStopSelected(stop) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape
                ) {
                    Column {
                        Text(
                            stop.stop_name,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = stop.stop_desc ?: "-",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            if (visibleStops.size == 20)
            {
                Text(
                    text = "Showing max 20 options",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BusstoptrackerTheme {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    BusstoptrackerTheme {
        val mockStops = listOf(
            Stop("1", "Pramogų arena", "Smth", null, null),
            Stop("2",  "Stotis", "More", null, null),
            Stop("3", "Didlaukio", "Written", null, null),
        )

        SearchScreen(
            allStops = mockStops,
            onStopSelected = {},
            onBack = {}
        )
    }
}