package com.lubna.carrideshare

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SearchRidesScreen(
    navController: NavController,
    factory: Factory
) {
    val vm: SearchRidesViewModel = viewModel(factory = factory)

    val query by vm.searchQuery.collectAsState()
    val rides by vm.filteredRides.collectAsState()
    val isLoading by vm.isLoading.collectAsState(initial = false)
    val errorMessage by vm.errorMessage.collectAsState(initial = null)

    Column(Modifier.fillMaxSize()) {
        TextField(
            value = query,
            onValueChange = { vm.onSearchQueryChange(it) },
            label = { Text("Search by Ride ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                rides.isEmpty() -> {
                    Text(
                        text = "No results found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(rides) { ride ->
                            RideShareItem(
                                rideName = ride.name,
                                departureTime = ride.taxiArrivalTime,
                                onClick = { navController.navigate("rideDetails/${ride.id}") }
                            )
                        }
                    }
                }
            }
        }
    }
}
