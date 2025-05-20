package com.lubna.carrideshare

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.gson.Gson
import kotlinx.coroutines.time.delay
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Dashboard(navController: NavController) {

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    var rideItems by remember { mutableStateOf<List<RideItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var myRideItems by remember { mutableStateOf<List<RideItem>>(emptyList()) }
    var isMineLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            try {
                // Fetch latest rides
                val response = RetrofitInstance.api.getLatestRides()
                if (response.isSuccessful) {
                    response.body()?.let { ridesResponse ->
                        rideItems = ridesResponse.rides
                    }
                } else {
                    println("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }

            try {
                // Fetch my latest rides
                val userDataJson = sharedPref.getString("userData", null)
                val userId = if (userDataJson != null) {
                    val gson = Gson()
                    val user = gson.fromJson(userDataJson, LoginResponse::class.java)
                    user.id
                } else {
                    ""
                }

                if (userId.isNotEmpty()) {
                    val response = RetrofitInstance.api.getMyLatestRides(userId = userId)
                    if (response.isSuccessful) {
                        response.body()?.let { ridesResponse ->
                            myRideItems = ridesResponse.rides
                        }
                    } else {
                        println("Error: ${response.code()} - ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isMineLoading = false
            }

            delay(Duration.ofSeconds(10)) // wait 10 seconds before next fetch
        }
    }

    Box {
        YellowBackground()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Row {
            Text(
                text = "Dashboard",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    navController.navigate("searchRides")
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search Logo",
                    modifier = Modifier.height(50.dp)
                )
            }

            IconButton(
                onClick = {
                    navController.navigate("settings")
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gear),
                    contentDescription = "Settings Logo",
                    modifier = Modifier.height(50.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(text = "Available Ride Shares", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(10.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (rideItems.isEmpty()) {
                Text(
                    text = "There are no new rides",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy((-10).dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(rideItems) { ride ->
                        RideShareItem(
                            rideName = ride.name,
                            departureTime = ride.taxiArrivalTime,
                            onClick = {
                                navController.navigate("rideDetails/${ride.id}")
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))


        Button(
            onClick = {
                navController.navigate("newrideshare")
            },
            colors = ButtonDefaults.buttonColors(Color.Black),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
                .align(Alignment.CenterHorizontally)

        ) {
            Text(text = "New Ride Share", fontSize = 18.sp, color = Color.White)
        }

        Spacer(Modifier.height(20.dp))

        Text(text = "My Ride Shares", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(10.dp))

        if (isMineLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (myRideItems.isEmpty()) {
                Text(
                    text = "You have no new rides",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy((-10).dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(myRideItems) { ride ->
                        RideShareItem(
                            rideName = ride.name,
                            departureTime = ride.taxiArrivalTime,
                            onClick = {
                                navController.navigate("rideDetails/${ride.id}")
                            }
                        )
                    }
                }
            }
        }

    }
}