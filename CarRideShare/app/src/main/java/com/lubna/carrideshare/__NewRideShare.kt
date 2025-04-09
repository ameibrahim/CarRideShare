//package com.lubna.carrideshare
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.navigation.NavController
//import org.osmdroid.config.Configuration
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory
//import org.osmdroid.util.GeoPoint
//import org.osmdroid.views.MapView
//import org.osmdroid.views.overlay.Marker
//
//// A simple data class to represent a location.
//data class Location(val address: String, val lat: Double, val lng: Double)
//
//// Dummy list of available locations.
//val availableLocations = listOf(
//    Location("123 Main St", 37.7749, -122.4194),
//    Location("456 Elm St", 37.7849, -122.4094),
//    Location("789 Maple Ave", 37.7649, -122.4294)
//)
//
///**
// * The custom yellow background composable.
// */
//@Composable
//fun YellowBackgroundComposable(content: @Composable () -> Unit) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Yellow)
//    ) {
//        content()
//    }
//}
//
///**
// * OsmMap composable using OSMDroid's MapView.
// * This displays an OpenStreetMap-based map and updates markers
// * for the rideFrom and rideTo locations.
// */
//@Composable
//fun OsmMap(
//    rideFromLocation: Location?,
//    rideToLocation: Location?
//) {
//    val context = LocalContext.current
//
//    // Initialize OSMDroid configuration. Typically this is done in Application.onCreate.
//    LaunchedEffect(context) {
//        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
//    }
//
//    // Remember the MapView to avoid recreating it on every recomposition.
//    val mapView = remember {
//        MapView(context).apply {
//            setTileSource(TileSourceFactory.MAPNIK)
//            controller.setZoom(13.0)
//            controller.setCenter(GeoPoint(37.7749, -122.4194))
//        }
//    }
//
//    AndroidView(
//        factory = { mapView },
//        modifier = Modifier.fillMaxSize(),
//        update = { map ->
//            // Clear previous markers.
//            map.overlays.clear()
//
//            // Add a marker for the rideFrom location if available.
//            rideFromLocation?.let {
//                val rideFromMarker = Marker(map).apply {
//                    position = GeoPoint(it.lat, it.lng)
//                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//                    title = "Ride From"
//                }
//                map.overlays.add(rideFromMarker)
//            }
//
//            // Add a marker for the rideTo location if available.
//            rideToLocation?.let {
//                val rideToMarker = Marker(map).apply {
//                    position = GeoPoint(it.lat, it.lng)
//                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//                    title = "Ride To"
//                }
//                map.overlays.add(rideToMarker)
//            }
//            map.invalidate() // Refresh the map view.
//        }
//    )
//}
//
///**
// * The NewRideShareScreen composable implements the complete view.
// * It contains the map with overlaid inputs for "Ride From" and "Ride To", along with other ride details.
// */
//@Composable
//fun NewRideShareScreen(navController: NavController) {
//    // State variables for the "Ride From" input.
//    var rideFromText by remember { mutableStateOf("") }
//    var rideFromExpanded by remember { mutableStateOf(false) }
//    var rideFromSelected by remember { mutableStateOf<Location?>(null) }
//
//    // State variables for the "Ride To" input.
//    var rideToText by remember { mutableStateOf("") }
//    var rideToExpanded by remember { mutableStateOf(false) }
//    var rideToSelected by remember { mutableStateOf<Location?>(null) }
//
//    // Additional input states.
//    var departureTime by remember { mutableStateOf("") }
//    var seatsTotal by remember { mutableStateOf("") }
//    var seatsAvailable by remember { mutableStateOf("") }
//    var totalPrice by remember { mutableStateOf("") }
//
//    // Filter available locations based on user input.
//    val filteredRideFrom = availableLocations.filter { it.address.contains(rideFromText, ignoreCase = true) }
//    val filteredRideTo = availableLocations.filter { it.address.contains(rideToText, ignoreCase = true) }
//
//    YellowBackgroundComposable {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            // The top section with the map.
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp)
//            ) {
//                // Display the OSMDroid map. Pass the selected locations.
//                OsmMap(
//                    rideFromLocation = rideFromSelected,
//                    rideToLocation = rideToSelected
//                )
//                // Overlay the "Ride From" and "Ride To" inputs at the top center.
//                Column(
//                    modifier = Modifier
//                        .align(Alignment.TopCenter)
//                        .padding(16.dp)
//                ) {
//                    // Ride From Input Field with dropdown suggestions.
//                    OutlinedTextField(
//                        value = rideFromText,
//                        onValueChange = {
//                            rideFromText = it
//                            rideFromExpanded = true
//                        },
//                        label = { Text("Ride From") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    DropdownMenu(
//                        expanded = rideFromExpanded,
//                        onDismissRequest = { rideFromExpanded = false },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        filteredRideFrom.forEach { location ->
//                            DropdownMenuItem(
//                                onClick = {
//                                    rideFromText = location.address
//                                    rideFromSelected = location
//                                    rideFromExpanded = false
//                                },
//                                text = { Text(text = location.address) }
//                            )
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    // Ride To Input Field with dropdown suggestions.
//                    OutlinedTextField(
//                        value = rideToText,
//                        onValueChange = {
//                            rideToText = it
//                            rideToExpanded = true
//                        },
//                        label = { Text("Ride To") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    DropdownMenu(
//                        expanded = rideToExpanded,
//                        onDismissRequest = { rideToExpanded = false },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        filteredRideTo.forEach { location ->
//                            DropdownMenuItem(
//                                onClick = {
//                                    rideToText = location.address
//                                    rideToSelected = location
//                                    rideToExpanded = false
//                                },
//                                text = { Text(text = location.address) }
//                            )
//                        }
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//            // Additional ride details input fields.
//            OutlinedTextField(
//                value = departureTime,
//                onValueChange = { departureTime = it },
//                label = { Text("Departure Time") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            OutlinedTextField(
//                value = seatsTotal,
//                onValueChange = { seatsTotal = it },
//                label = { Text("Total Seats") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            OutlinedTextField(
//                value = seatsAvailable,
//                onValueChange = { seatsAvailable = it },
//                label = { Text("Available Seats") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            OutlinedTextField(
//                value = totalPrice,
//                onValueChange = { totalPrice = it },
//                label = { Text("Total Price") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(
//                onClick = {
//                    // Handle the confirmation of the new ride.
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Confirm New Ride")
//            }
//        }
//    }
//}