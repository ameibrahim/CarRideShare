package com.lubna.carrideshare

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

// --- Utility Functions ---

fun getScaledBitmap(context: Context, resId: Int, desiredWidth: Int, desiredHeight: Int): Bitmap {
    val originalBitmap = BitmapFactory.decodeResource(context.resources, resId)
    return Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, false)
}

// --- Data Models and Dummy Data ---

data class Location(val address: String, val lat: Double, val lng: Double)

val availableLocations = listOf(
    Location("123 Main St", 37.7749, -122.4194),
    Location("456 Elm St", 37.7849, -122.4094),
    Location("789 Maple Ave", 37.7649, -122.4294)
)

// --- Helper Functions for Routing ---

fun decodePolyline(encoded: String, precision: Double = 1e6): List<Point> {
    val poly = mutableListOf<Point>()
    var index = 0
    val len = encoded.length
    var lat = 0L
    var lng = 0L

    while (index < len) {
        var result = 0
        var shift = 0
        var b: Int
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1F) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lat += dlat

        result = 0
        shift = 0
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1F) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lng += dlng

        poly.add(Point.fromLngLat(lng.toDouble() / precision, lat.toDouble() / precision))
    }
    return poly
}

suspend fun fetchRoute(rideFrom: Location, rideTo: Location): List<Point>? {
    return withContext(Dispatchers.IO) {
        try {
            // Replace with your valid Mapbox access token.
            val accessToken = "pk.eyJ1IjoiaWJyYWhpbWFtZTEzIiwiYSI6ImNsb2xlaDUxbDJlcXYya3A5bzZoZWc5MzkifQ.YyKfquv1mvX7xrUj5oG1Ow"
            val url = "https://api.mapbox.com/directions/v5/mapbox/driving/" +
                    "${rideFrom.lng},${rideFrom.lat};${rideTo.lng},${rideTo.lat}" +
                    "?alternatives=false&geometries=polyline6&steps=false&access_token=$accessToken"

            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string()?.let { bodyStr ->
                        val json = JSONObject(bodyStr)
                        val routes = json.getJSONArray("routes")
                        if (routes.length() > 0) {
                            val firstRoute = routes.getJSONObject(0)
                            val geometry = firstRoute.getString("geometry")
                            return@withContext decodePolyline(geometry, precision = 1e6)
                        }
                    }
                }
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

// --- UI Composables ---

@Composable
fun YellowBackgroundComposable(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ) {
        content()
    }
}

@Composable
fun MapboxMap(
    rideFromLocation: Location?,
    rideToLocation: Location?
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            mapboxMap.loadStyleUri(Style.MAPBOX_STREETS)
        }
    }

    // State to hold the fetched route points.
    var routePoints by remember { mutableStateOf<List<Point>?>(null) }
    LaunchedEffect(rideFromLocation, rideToLocation) {
        if (rideFromLocation != null && rideToLocation != null) {
            routePoints = fetchRoute(rideFromLocation, rideToLocation)
        } else {
            routePoints = null
        }
    }

    // Remember the annotation managers to ensure we reuse them across updates.
    val pointAnnotationManager = remember { mapView.annotations.createPointAnnotationManager() }
    val polylineAnnotationManager = remember { mapView.annotations.createPolylineAnnotationManager() }

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize(),
        update = { map ->
            map.mapboxMap.getStyle { style ->
                // Load marker images if not already loaded.
                if (style.getStyleImage("marker") == null) {
                    val markerBitmap = getScaledBitmap(context, R.drawable.marker, 48, 48)
                    style.addImage("marker", markerBitmap)
                }
                if (style.getStyleImage("circle") == null) {
                    val circleBitmap = getScaledBitmap(context, R.drawable.circle, 128, 128)
                    style.addImage("circle", circleBitmap)
                }

                // Clear previous annotations.
                pointAnnotationManager.deleteAll()
                polylineAnnotationManager.deleteAll()

                // Add markers.
                rideFromLocation?.let { location ->
                    val rideFromAnnotationOptions = PointAnnotationOptions()
                        .withPoint(Point.fromLngLat(location.lng, location.lat))
                        .withIconImage("marker")
                    pointAnnotationManager.create(rideFromAnnotationOptions)
                }
                rideToLocation?.let { location ->
                    val rideToAnnotationOptions = PointAnnotationOptions()
                        .withPoint(Point.fromLngLat(location.lng, location.lat))
                        .withIconImage("circle")
                    pointAnnotationManager.create(rideToAnnotationOptions)
                }

                // Draw the routed path if available.
                routePoints?.let { points ->
                    val polylineOptions = PolylineAnnotationOptions()
                        .withPoints(points)
                        .withLineColor("#FF0000") // Red route line.
                        .withLineWidth(4.0)
                    polylineAnnotationManager.create(polylineOptions)

                    // Update the camera to include the entire route.
                    val cameraOptions = map.mapboxMap.cameraForCoordinates(
                        points,
                        EdgeInsets(64.0, 64.0, 64.0, 64.0),
                        0.0,
                        0.0
                    )
                    map.getMapboxMap().setCamera(cameraOptions)
                }
            }
        }
    )
}

@Composable
fun NewRideShareScreen(navController: NavController) {
    var rideFromText by remember { mutableStateOf("") }
    var rideFromExpanded by remember { mutableStateOf(false) }
    var rideFromSelected by remember { mutableStateOf<Location?>(null) }

    var rideToText by remember { mutableStateOf("") }
    var rideToExpanded by remember { mutableStateOf(false) }
    var rideToSelected by remember { mutableStateOf<Location?>(null) }

    var departureTime by remember { mutableStateOf("") }
    var seatsTotal by remember { mutableStateOf("") }
    var seatsAvailable by remember { mutableStateOf("") }
    var totalPrice by remember { mutableStateOf("") }

    val filteredRideFrom = availableLocations.filter {
        it.address.contains(rideFromText, ignoreCase = true)
    }
    val filteredRideTo = availableLocations.filter {
        it.address.contains(rideToText, ignoreCase = true)
    }

    YellowBackgroundComposable {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                MapboxMap(
                    rideFromLocation = rideFromSelected,
                    rideToLocation = rideToSelected
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = rideFromText,
                        onValueChange = {
                            rideFromText = it
                            rideFromExpanded = true
                        },
                        label = { Text("Ride From") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = rideFromExpanded,
                        onDismissRequest = { rideFromExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        filteredRideFrom.forEach { location ->
                            DropdownMenuItem(
                                onClick = {
                                    rideFromText = location.address
                                    rideFromSelected = location
                                    rideFromExpanded = false
                                },
                                text = { Text(text = location.address) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = rideToText,
                        onValueChange = {
                            rideToText = it
                            rideToExpanded = true
                        },
                        label = { Text("Ride To") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = rideToExpanded,
                        onDismissRequest = { rideToExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        filteredRideTo.forEach { location ->
                            DropdownMenuItem(
                                onClick = {
                                    rideToText = location.address
                                    rideToSelected = location
                                    rideToExpanded = false
                                },
                                text = { Text(text = location.address) }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = departureTime,
                onValueChange = { departureTime = it },
                label = { Text("Departure Time") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = seatsTotal,
                onValueChange = { seatsTotal = it },
                label = { Text("Total Seats") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = seatsAvailable,
                onValueChange = { seatsAvailable = it },
                label = { Text("Available Seats") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = totalPrice,
                onValueChange = { totalPrice = it },
                label = { Text("Total Price") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Handle confirmation of the new ride.
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm New Ride")
            }
        }
    }
}
