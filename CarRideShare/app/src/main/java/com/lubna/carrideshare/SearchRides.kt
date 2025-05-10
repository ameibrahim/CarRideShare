package com.lubna.carrideshare

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.navigation.NavController

@Composable
fun SearchRidesScreen(
    navController: NavController,
    factory: Factory
) {
    // instantiate your VM with the provided factory
    val vm: SearchRidesViewModel = viewModel(factory = factory)

    // collect StateFlows as Compose state
    val query by vm.searchQuery.collectAsState()
    val rides by vm.filteredRides.collectAsState()

    Column(Modifier.fillMaxSize()) {
        TextField(
            value = query,
            onValueChange = vm::onSearchQueryChange,
            label = { Text("Search by Ride ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn(Modifier.fillMaxSize()) {
            items(rides) { ride ->
                Text(
                    text = ride.id,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("rideDetails/${ride.id}")
                        }
                        .padding(16.dp)
                )
                HorizontalDivider()
            }
        }
    }
}