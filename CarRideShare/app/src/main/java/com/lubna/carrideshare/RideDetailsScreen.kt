package com.lubna.carrideshare

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.rememberCoroutineScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.navigation.NavController

@Composable
fun RideDetailsScreen(rideId: String, navController: NavController) {

    val scrollState = rememberScrollState()

    var rideDetails by remember { mutableStateOf<RideDetailsResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    val scope = rememberCoroutineScope()

    // Retrieve the user data JSON from SharedPreferences and parse it
    val userDataJson = sharedPrefs.getString("userData", null)
    val currentUserId = if (userDataJson != null) {
        val gson = Gson()
        val user = gson.fromJson(userDataJson, LoginResponse::class.java)
        user.id
    } else {
        ""
    }

    LaunchedEffect(rideId) {
        while (true) {
            try {
                val response = RetrofitInstance.api.getRideDetails(rideId)
                if (response.isSuccessful) {
                    rideDetails = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
            kotlinx.coroutines.delay(4000) // wait 5 seconds before refreshing
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        rideDetails?.let { ride ->

            Box(modifier = Modifier.fillMaxSize()) {
                YellowBackground()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(20.dp)
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = ride.name,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Status: ${ride.status.lowercase()} - ${ride.taxiArrivalTime}",
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.W400
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = ride.startLocation,
                            fontSize = 24.sp,
                            color = Color(0xFF467864),
                            fontWeight = FontWeight.W800,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Image(
                            painter = painterResource(id = R.drawable.circuit),
                            contentDescription = "Search Logo",
                            modifier = Modifier.height(50.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = ride.endLocation,
                            fontSize = 24.sp,
                            color = Color(0xFFE13636),
                            fontWeight = FontWeight.W800,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (ride.createdById == currentUserId) {
                            if (ride.status == "SCHEDULED") {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            try {
                                                val response =
                                                    RetrofitInstance.api.updateRideStatus(
                                                        rideId = ride.id,
                                                        statusUpdate = mapOf("status" to "ONGOING")
                                                    )
                                                if (response.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Ride started",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to start ride",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } catch (e: Exception) {
                                                Toast.makeText(
                                                    context,
                                                    "Error: ${e.localizedMessage}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(Color.Black),
                                    shape = RoundedCornerShape(40.dp),
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height(40.dp)
                                ) {
                                    Text(text = "Start Ride", fontSize = 13.sp, color = Color.White)
                                }
                            } else if (ride.status == "ONGOING") {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            try {
                                                val response =
                                                    RetrofitInstance.api.updateRideStatus(
                                                        rideId = ride.id,
                                                        statusUpdate = mapOf("status" to "ENDED")
                                                    )
                                                if (response.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Ride Ended",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to end ride",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } catch (e: Exception) {
                                                Toast.makeText(
                                                    context,
                                                    "Error: ${e.localizedMessage}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(Color.Red),
                                    shape = RoundedCornerShape(40.dp),
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height(40.dp)
                                ) {
                                    Text(text = "End Ride", fontSize = 13.sp, color = Color.White)
                                }
                            }
                        } else {
                            val hasBooking = ride.bookings.any { it.userId == currentUserId }

                            when {
                                hasBooking -> {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    val myBooking =
                                                        ride.bookings.find { it.userId == currentUserId }

                                                    val response =
                                                        RetrofitInstance.api.cancelBooking(
                                                            bookingId = myBooking?.id
                                                                ?: return@launch
                                                        )
                                                    if (response.isSuccessful) {
                                                        Toast.makeText(
                                                            context,
                                                            "Booking cancelled",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Failed to cancel",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                } catch (e: Exception) {
                                                    Toast.makeText(
                                                        context,
                                                        "Error: ${e.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(Color.Red),
                                        shape = RoundedCornerShape(40.dp),
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .height(40.dp)
                                    ) {
                                        Text(
                                            "Cancel Booking",
                                            fontSize = 13.sp,
                                            color = Color.White
                                        )
                                    }
                                }

                                ride.availableSeats > 0 -> {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    val response =
                                                        RetrofitInstance.api.createBooking(
                                                            bookingRequest = BookingRequest(
                                                                rideId = ride.id,
                                                                userId = currentUserId
                                                                    ?: return@launch,
                                                                costPerPassenger = ride.cost // this is a string
                                                            )
                                                        )
                                                    if (response.isSuccessful) {
                                                        Toast.makeText(
                                                            context,
                                                            "Booking created",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Booking failed",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                } catch (e: Exception) {
                                                    Toast.makeText(
                                                        context,
                                                        "Error: ${e.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(Color.Green),
                                        shape = RoundedCornerShape(40.dp),
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .height(40.dp)
                                    ) {
                                        Text("Book Ride", fontSize = 13.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Ride Share Owner",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.W800
                    )

                        SimpleCardItem(
                            topText = ride.createdBy.fullName,
                            bottomText = ride.createdBy.email
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Seats Available",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.W800
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            CircleText("${ride.availableSeats}")

                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "out of")
                            Spacer(modifier = Modifier.width(10.dp))

                            CircleText("${ride.totalSeats}")

                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Cost",
                                fontSize = 26.sp,
                                color = Color.White,
                                fontWeight = FontWeight.W800
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                "${ride.cost} TL",
                                fontSize = 26.sp,
                                color = Color(0xFF467864),
                                fontWeight = FontWeight.W800
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            val perPersonCost = try {
                                val cost = ride.cost.toDouble()
                                val people = ride.bookings.size.coerceAtLeast(1) // prevent division by 0
                                (cost / people).toInt()
                            } catch (e: Exception) {
                                0
                            }

                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .height(40.dp)
                                    .wrapContentWidth()
                                    .align(Alignment.CenterHorizontally),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "≈ $perPersonCost TL / person",
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(Color(0xFF467864), shape = RoundedCornerShape(50.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }


                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Bookings: ${ride.bookings.size}",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.W800
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (ride.bookings.isEmpty()) {
                            Text(
                                text = "No one has booked this ride yet.",
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        } else {
                            ride.bookings.forEach { booking ->
                                SimpleCardItem(
                                    topText = booking.user.fullName,
                                    bottomText = booking.user.email
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            if (ride.createdById == currentUserId) {
                                if (ride.status == "SCHEDULED") {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    val response = RetrofitInstance.api.deleteRide(ride.id)
                                                    if (response.isSuccessful) {
                                                        Toast.makeText(context, "Ride deleted", Toast.LENGTH_SHORT).show()
                                                        navController.navigate("dashboard") {
                                                            popUpTo("dashboard") { inclusive = true }
                                                        }
                                                    } else {
                                                        Toast.makeText(context, "Failed to delete ride", Toast.LENGTH_SHORT).show()
                                                    }
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(Color.Red),
                                        shape = RoundedCornerShape(40.dp),
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .height(40.dp)
                                    ) {
                                        Text(
                                            text = "Delete Ride Share",
                                            fontSize = 13.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                        }
                }

            } ?: run {
                Text("Failed to load ride details.")
            }
        }
    }
}