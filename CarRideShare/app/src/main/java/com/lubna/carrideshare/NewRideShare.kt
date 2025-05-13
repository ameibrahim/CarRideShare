package com.lubna.carrideshare

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Calendar

@Composable
fun TimePickerField(
    timeText: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current

    OutlinedTextField(
        value = timeText,
        onValueChange = {},
        label = { Text("Taxi Arrival Time") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute))
                    },
                    hour,
                    minute,
                    true
                ).show()
            },
        readOnly = true
    )
}

@Composable
fun YellowBackgroundComposable(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFD157))
    ) {
        content()
    }
}

@Composable
fun NewRideShareScreen(navController: NavController) {
    var rideFromText by remember { mutableStateOf("") }
    var rideToText by remember { mutableStateOf("") }
    
    var departureTime by remember { mutableStateOf("Select Time") }
    var taxiCalled by remember { mutableStateOf(false) }
    var seatsTotal by remember { mutableStateOf("") }
    var seatsAvailable by remember { mutableStateOf("") }
    var totalPrice by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    val userDataJson = sharedPref.getString("userData", null)
    val currentUserId = if (userDataJson != null) {
        val gson = Gson()
        val user = gson.fromJson(userDataJson, LoginResponse::class.java)
        user.id
    } else {
        ""
    }

    val coroutineScope = rememberCoroutineScope()

    YellowBackgroundComposable {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "New Ride",
                fontSize = 40.sp,
                fontWeight = FontWeight.W800,
                color = Color.White,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(28.dp))


            OutlinedTextField(
                value = rideFromText,
                onValueChange = { rideFromText = it },
                label = { Text("Ride From") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(Color.White),
                shape = RoundedCornerShape(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = rideToText,
                onValueChange = { rideToText = it },
                label = { Text("Ride To") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(Color.White),
                shape = RoundedCornerShape(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            TimePickerField(
                timeText = departureTime,
                onTimeSelected = { selectedTime ->
                    departureTime = selectedTime
                }

            )
            Spacer(modifier = Modifier.height(8.dp))
            // Taxi Called Toggle Input.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(
                    checked = taxiCalled,
                    onCheckedChange = { taxiCalled = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Taxi Called")
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = seatsTotal,
                onValueChange = { seatsTotal = it },
                label = { Text("Total Seats") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(Color.White),
                shape = RoundedCornerShape(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = seatsAvailable,
                onValueChange = { seatsAvailable = it },
                label = { Text("Available Seats") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(Color.White),
                shape = RoundedCornerShape(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = totalPrice,
                onValueChange = { totalPrice = it },
                label = { Text("Cost") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(Color.White),
                shape = RoundedCornerShape(50.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val costValue = totalPrice.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    val totalSeatsValue = seatsTotal.toIntOrNull() ?: 0
                    val availableSeatsValue = seatsAvailable.toIntOrNull() ?: 0
                    val taxiArrivalValue = if (taxiCalled && departureTime != "Select Time") departureTime else ""

                    val rideRequest = RideRequest(
                        createdById = currentUserId,
                        startLocation = rideFromText,
                        endLocation = rideToText,
                        taxiCalled = taxiCalled,
                        taxiArrivalTime = taxiArrivalValue,
                        cost = costValue,
                        totalSeats = totalSeatsValue,
                        availableSeats = availableSeatsValue
                    )
                    coroutineScope.launch {
                        val response = RetrofitInstance.api.createRide(rideRequest)
                        if (response.isSuccessful) {
                            println("Ride created successfully!")
                        } else {
                            println("Error creating ride: ${response.errorBody()?.string()}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.Black,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                Text("Confirm New Ride")
            }
        }
    }
}